import chisel3._
import chisel3.util._

class UdpHead extends Bundle {
  val src_port = UInt(16.W)
  val dst_port = UInt(16.W)
  val length = UInt(16.W)
  val checksum = UInt(16.W)
}

class TcpHead extends Bundle {
  val src_port = UInt(16.W)
  val dst_port = UInt(16.W)
  val seq_num = UInt(32.W)
  val ack_num = UInt(32.W)
  val head_length = UInt(4.W) // 4 bits for head length
  val reserved = UInt(4.W) // 3 bits reserved
  val cwr = UInt(1.W)
  val ece = UInt(1.W)
  val urg = UInt(1.W)
  val ack = UInt(1.W)
  val psh = UInt(1.W)
  val rst = UInt(1.W)
  val syn = UInt(1.W)
  val fin = UInt(1.W)
  val window_size = UInt(16.W)
  val checksum = UInt(16.W)
  val urgent_pointer = UInt(16.W)
}

class PipelineConnectIO extends Bundle {
  // common
  val data = Output(UInt(64.W))
  // udp head signals
  val udp_head = Output(new UdpHead)
  // tcp head signals
  val tcp_head = Output(new TcpHead)
}