import chisel3._
import chisel3.util._
import PipelineConnect._

class Top extends Module {
  val io = IO(new Bundle {
    val axis = Flipped(new AXIS)
    val out = new PipelineConnectIO
  })

  val axi_analysis = Module(new AxiAnalysis)

  val udp = Module(new Udp)

  io.axis <> axi_analysis.io.in
  io.out := udp.io.out.bits
  udp.io.out.ready := true.B
  axi_analysis.io.out_tcp.ready := false.B // Not used in this example

  PipelineConnect(axi_analysis.io.out_udp, udp.io.in, udp.io.out.fire, false.B)
  dontTouch(io.out)
}