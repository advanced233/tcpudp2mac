import chisel3._
import Config._

class AxiAnalysis extends Module {
  import chisel3.util._
  val io = IO(new Bundle {
    val in = Flipped(new AXIS)
    val out_udp = Decoupled(new PipelineConnectIO)
    val out_tcp = Decoupled(new PipelineConnectIO)
  })

  io.out_udp.bits := DontCare
  io.out_tcp.bits := DontCare
  io.in.tready := DontCare
  io.out_udp.valid := DontCare
  io.out_tcp.valid := DontCare

  // analysis axi-stream
  // state machine
  val idle :: receive :: send_udp :: send_tcp :: Nil = Enum(4)
  val state = RegInit(idle)

  state := MuxLookup(state, idle)(Seq(
    idle -> Mux(io.in.tvalid && io.in.tready, receive, idle),
    receive -> Mux(io.in.tvalid && io.in.tready && io.in.tlast, Mux(io.in.tdata(AXI_DATA_WIDTH - 1), send_tcp, send_udp), receive),
    send_udp -> Mux(io.out_udp.fire, idle, send_udp),
    send_tcp -> Mux(io.out_tcp.fire, idle, send_tcp)
  ))

  val data_reg = RegInit(VecInit(Seq.fill(MAX_DATA_WIDTH)(0.U(8.W))))
  val write_pointer = RegInit(0.U(16.W)) // write pointer for data
  when(state === idle) {
    io.in.tready := true.B
    io.out_udp.valid := false.B
    io.out_tcp.valid := false.B
    when(io.in.tvalid && io.in.tready) {
      data_reg(write_pointer) := io.in.tdata(7, 0)
      data_reg(write_pointer + 1.U) := io.in.tdata(15, 8)
      data_reg(write_pointer + 2.U) := io.in.tdata(23, 16)
      data_reg(write_pointer + 3.U) := io.in.tdata(31, 24)
      write_pointer := write_pointer + 4.U
    }
  }
  when(state === receive) {
    io.in.tready := true.B
    io.out_udp.valid := false.B
    io.out_tcp.valid := false.B

    data_reg(write_pointer) := io.in.tdata(7, 0)
    data_reg(write_pointer + 1.U) := io.in.tdata(15, 8)
    data_reg(write_pointer + 2.U) := io.in.tdata(23, 16)
    data_reg(write_pointer + 3.U) := io.in.tdata(31, 24)
    write_pointer := write_pointer + 4.U
  }
  when(state === send_udp) {
    io.in.tready := false.B
    io.out_udp.valid := true.B
    io.out_tcp.valid := false.B

    io.out_udp.bits.data := data_reg.asTypeOf(UInt((8 * MAX_DATA_WIDTH).W))
    io.out_udp.bits.len := write_pointer
    write_pointer := 0.U
  }
  when(state === send_tcp) {
    io.in.tready := false.B
    io.out_udp.valid := false.B
    io.out_tcp.valid := true.B

    io.out_tcp.bits.data := data_reg.asTypeOf(UInt((8 * MAX_DATA_WIDTH).W))
    io.out_tcp.bits.len := write_pointer
    write_pointer := 0.U
  }
}

object AAA extends App {
  val firtoolOptions = Array(
    "--lowering-options=" + List(
      // make yosys happy
      // see https://github.com/llvm/circt/blob/main/docs/VerilogGeneration.md
      "disallowLocalVariables",
      "disallowPackedArrays",
      "locationInfoStyle=wrapInAtSquareBracket"
    ).reduce(_ + "," + _)
  )
  circt.stage.ChiselStage.emitSystemVerilogFile(new AxiAnalysis(), args, firtoolOptions)
}