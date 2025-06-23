import chisel3._
import chisel3.util._
import Config._

class CheckSum extends Module {
  val io = IO(new Bundle {
    val in  = Flipped(Decoupled(new PipelineConnectIO))
    val out = Decoupled(new PipelineConnectIO)
  })

  io.out.bits := io.in.bits
  io.in.ready  := false.B
  io.out.valid := false.B

  val header_reg      = Reg(UInt(((12 + 20) * 8).W))
  val payload_reg     = Reg(UInt((MAX_DATA_WIDTH * 8).W))
  val header_words    = Reg(UInt(9.W))   // 单位为16b
  val payload_words   = Reg(UInt(9.W))   // 单位为16b
  val running_sum     = RegInit(0.U(17.W))

  val debug_header_reg = header_reg(255, 0)
  val debug_payload_reg = payload_reg(255, 0)
  dontTouch(debug_header_reg)
  dontTouch(debug_payload_reg)

  val idle :: calc_header :: calc_payload :: done :: Nil = Enum(4)
  val state = RegInit(idle)

  state := MuxLookup(state, idle)(Seq(
    idle         -> Mux(io.in.valid, calc_header, idle),
    calc_header  -> Mux(header_words === 0.U, calc_payload, calc_header),
    calc_payload -> Mux(payload_words === 0.U, done, calc_payload),
    done         -> Mux(io.out.fire, idle, done)
  ))

  switch(state) {
    is(idle) {
      io.in.ready := true.B
      when(io.in.valid) {
        io.in.ready := false.B
        val pseudo = Cat(
          0.U(32.W),         // src IP
          0.U(32.W),         // dst IP
          0.U(8.W),          // zero
          6.U(8.W),          // protocol = TCP
          io.in.bits.len + 20.U(16.W)  // TCP length = header(20) + data
        )
        val tcp_hdr = io.in.bits.tcp_head.asUInt

        header_reg   := Cat(pseudo, tcp_hdr)
        header_words := ((12 + 20) * 8 / 16).U

        val byte_len    = io.in.bits.len
        val padded_data = Mux(byte_len(0) === 1.U,
                             Cat(io.in.bits.data, 0.U(8.W)),
                             io.in.bits.data)
        payload_reg   := padded_data
        payload_words := ((byte_len + 1.U) >> 1) // upper 16 位字数

        running_sum := 0.U
      }
    }

    is(calc_header) {
      when(header_words =/= 0.U) {
        val next = running_sum +& header_reg(15, 0)
        running_sum := next +& next(16)
        header_reg   := header_reg >> 16
        header_words := header_words - 1.U
      }
    }

    is(calc_payload) {
      when(payload_words =/= 0.U) {
        val next = running_sum +& payload_reg(15, 0)
        running_sum := next +& next(16)
        payload_reg   := payload_reg >> 16
        payload_words := payload_words - 1.U
      }
    }

    is(done) {
      val final_sum = (running_sum & 0xFFFF.U) +& (running_sum >> 16)
      io.out.bits.tcp_head.checksum := ~final_sum(15, 0)
      io.out.valid := true.B
    }
  }
}
