import chisel3._
import chisel3.util._

/* 计算前缀和，看掩码的低 idx 位，计算里面有多少个 1 */
object PreFixSum {
  def prefixSum(mask: UInt, idx: Int): UInt = {
    PopCount(mask(idx - 1, 0))
  }
}