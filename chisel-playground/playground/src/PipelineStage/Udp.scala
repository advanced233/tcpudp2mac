import chisel3._
import Config._
import chisel3.util._
import chisel3.util.random._

class Udp extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new PipelineConnectIO))
    val out = Decoupled(new PipelineConnectIO)
  })

  io.out.bits := io.in.bits

  val lfsr1 = LFSR(16)
  val lfsr2 = LFSR(16)
  
  io.out.bits.udp_head.src_port := lfsr1
  io.out.bits.udp_head.dst_port := lfsr2

  io.out.bits.udp_head.length := io.in.bits.len + 8.U
  io.out.bits.udp_head.checksum := 0.U // Checksum calculation not implemented

  io.out.valid := io.in.valid
  io.in.ready := !io.in.valid || io.out.fire
}