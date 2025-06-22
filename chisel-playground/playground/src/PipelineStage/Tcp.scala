import chisel3._
import Config._
import chisel3.util._
import chisel3.util.random._

class Tcp extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new PipelineConnectIO))
    val out = Decoupled(new PipelineConnectIO)
  })

  io.out.bits := io.in.bits

  io.out.valid := io.in.valid
  io.in.ready := !io.in.valid || io.out.fire

  val lfsr1 = LFSR(16)
  val lfsr2 = LFSR(16)
  val lfsr3 = LFSR(16)

  io.out.bits.tcp_head.src_port := lfsr1
  io.out.bits.tcp_head.dst_port := lfsr2
  io.out.bits.tcp_head.seq_num := lfsr3
  io.out.bits.tcp_head.ack_num := 0.U
  io.out.bits.tcp_head.head_length := 5.U // 5 * 4 = 20 bytes
  io.out.bits.tcp_head.reserved := 0.U
  io.out.bits.tcp_head.cwr := 0.U
  io.out.bits.tcp_head.ece := 0.U
  io.out.bits.tcp_head.urg := 0.U
  io.out.bits.tcp_head.ack := 0.U
  io.out.bits.tcp_head.psh := 0.U
  io.out.bits.tcp_head.rst := 0.U
  io.out.bits.tcp_head.syn := 0.U
  io.out.bits.tcp_head.fin := 0.U
  io.out.bits.tcp_head.window_size := 0.U // Window size not implemented
  io.out.bits.tcp_head.checksum := 0.U // Checksum calculation not implemented
  io.out.bits.tcp_head.urgent_pointer := 0.U // Urgent pointer not implemented
}