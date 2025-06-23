import chisel3._
import chisel3.util._
import PipelineConnect._

class Top extends Module {
  val io = IO(new Bundle {
    val axis = Flipped(new AXIS)
    val out1 = new PipelineConnectIO
    val out2 = new PipelineConnectIO
  })

  val axi_analysis = Module(new AxiAnalysis)

  val udp = Module(new Udp)

  val tcp = Module(new Tcp)

  val check_sum = Module(new CheckSum)

  io.axis <> axi_analysis.io.in
  io.out1 := udp.io.out.bits
  io.out2 := check_sum.io.out.bits
  udp.io.out.ready := true.B
  check_sum.io.out.ready := true.B 

  PipelineConnect(axi_analysis.io.out_udp, udp.io.in, udp.io.out.fire, false.B)
  PipelineConnect(axi_analysis.io.out_tcp, tcp.io.in, tcp.io.out.fire, false.B)
  PipelineConnect(tcp.io.out, check_sum.io.in, check_sum.io.out.fire, false.B)
  dontTouch(io.out1)
  dontTouch(io.out2)
}