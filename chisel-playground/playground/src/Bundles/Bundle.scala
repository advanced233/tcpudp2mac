import chisel3._
import chisel3.util._
import Config._

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
  // 不实现选项字段
}

class IpHead extends Bundle {
  val version = UInt(4.W) // 4 bits for version
  val ihl = UInt(4.W) // 4 bits for Internet Header Length
  val dscp = UInt(6.W) // 6 bits for Differentiated Services Code Point
  val ecn = UInt(2.W) // 2 bits for Explicit Congestion Notification
  val total_length = UInt(16.W)
  val identification = UInt(16.W)
  val flags = UInt(3.W) // 3 bits for flags
  val fragment_offset = UInt(13.W) // 13 bits for fragment offset
  val ttl = UInt(8.W) // Time to Live
  val protocol = UInt(8.W) // Protocol type (TCP, UDP, etc.)
  val header_checksum = UInt(16.W)
  val src_ip = UInt(32.W) // Source IP address
  val dst_ip = UInt(32.W) // Destination IP address
  // 不实现选项字段
}

class MacHead extends Bundle {
  // 前导码
  val preamble = UInt((8 * 7).W)
  val sfd = UInt(8.W) // Start Frame Delimiter
  val dst_mac = UInt((8 * 6).W) // Destination MAC address
  val src_mac = UInt((8 * 6).W) // Source MAC address
  val ethertype = UInt((8 * 2).W) // Ethertype field
}

class MacTail extends Bundle {
  val crc_check = UInt((8 * 4).W)
}

class AXIS extends Bundle {     // master
  val tvalid = Output(Bool())
  val tready = Input(Bool())
  val tdata = Output(UInt(AXI_DATA_WIDTH.W))
  val tkeep = Output(UInt((AXI_DATA_WIDTH / 8).W))
  val tstrb = Output(UInt((AXI_DATA_WIDTH / 8).W))
  val tlast = Output(Bool())

  // 没卵用
  val tid = Output(UInt((AXI_DATA_WIDTH / 8).W))
  val tdest = Output(UInt((AXI_DATA_WIDTH / 8).W))
  val tuser = Output(UInt((AXI_DATA_WIDTH / 8).W))
}

class PipelineConnectIO extends Bundle {
  // common
  val data = Output(UInt((8 * MAX_DATA_WIDTH).W))
  val len = Output(UInt(16.W)) // length of the data
  // udp head signals
  val udp_head = Output(new UdpHead)
  // tcp head signals
  val tcp_head = Output(new TcpHead)
  // ip head signals
  val ip_head = Output(new IpHead)
  // mac head signals
  val mac_head = Output(new MacHead)
  // mac tail signals
  val mac_tail = Output(new MacTail)
}