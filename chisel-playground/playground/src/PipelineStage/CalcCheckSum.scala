import chisel3._
import Config._
import chisel3.util._

class CheckSum extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new PipelineConnectIO))
    val out = Decoupled(new PipelineConnectIO)
  })

  io.out.bits := io.in.bits
  io.in.ready := false.B
  io.out.valid := false.B

  val latched_reg = Reg(UInt(((MAX_DATA_WIDTH + 20 + 12 + 1) * 8).W))
  val latched_length = RegInit(0.U(17.W)) // 20 bytes for TCP header + 12 bytes for pseudo header + data length

  val running_sum = RegInit(0.U(17.W))
  // state machine
  val idle :: calc :: done :: Nil = Enum(3)
  val state = RegInit(idle)

  state := MuxLookup(state, idle)(Seq(
    idle -> Mux(io.in.valid, calc, idle),
    calc -> Mux(latched_length === 0.U, done, calc),
    done -> Mux(io.out.fire, idle, done)
  ))

  switch(state) {
    is(idle) {
      io.out.valid := false.B
      io.in.ready := true.B
      when(io.in.valid) {
        io.in.ready := false.B
        val tmp_latched_length = io.in.bits.len + 20.U(16.W) + 12.U(16.W)

        // 不知道ip地址，设为0,后续补充
        val pseudo = Cat(0.U(32.W), 0.U(32.W), 0.U(8.W), 6.U(8.W), io.in.bits.len + 20.U)
        val tcp_hdr = io.in.bits.tcp_head.asUInt
        val payload = Mux(io.in.bits.len % 2.U === 0.U, 
                          io.in.bits.data, Cat(io.in.bits.data, 0.U(8.W)))

        latched_reg := Cat(pseudo, tcp_hdr, payload)
        latched_length := Mux(tmp_latched_length % 2.U === 0.U, 
                                  tmp_latched_length, 
                                  tmp_latched_length + 1.U)
        running_sum := 0.U
      }
    }
    is(calc) {
      when(latched_length =/= 0.U) {
        val next_sum = Wire(UInt(17.W))
        next_sum := running_sum +& latched_reg(15, 0)
        running_sum := next_sum(16) +& Cat(0.U(1.W), next_sum(15, 0))
        latched_length := latched_length - 2.U
        latched_reg := latched_reg >> 16
      }
    }
    is(done) {
      io.out.bits.tcp_head.checksum := ~(running_sum(15, 0))
      io.out.valid := true.B
    }
  }
}