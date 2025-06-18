error id: file://<WORKSPACE>/src/main/scala/gcd/GCD.scala:`<none>`.
file://<WORKSPACE>/src/main/scala/gcd/GCD.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1036
uri: file://<WORKSPACE>/src/main/scala/gcd/GCD.scala
text:
```scala
// See README.md for license details.

package gcd

import chisel3._
// _root_ disambiguates from package chisel3.util.circt if user imports chisel3.util._
import _root_.circt.stage.ChiselStage

/**
  * Compute GCD using subtraction method.
  * Subtracts the smaller from the larger until register y is zero.
  * value in register x is then the GCD
  */
class GCD extends Module {
  val io = IO(new Bundle {
    val value1        = Input(UInt(16.W))
    val value2        = Input(UInt(16.W))
    val loadingValues = Input(Bool())
    val outputGCD     = Output(UInt(16.W))
    val outputValid   = Output(Bool())
  })

  val x  = Reg(UInt())
  val y  = Reg(UInt())

  when(x > y) { x := x - y }
    .otherwise { y := y - x }

  when(io.loadingValues) {
    x := io.value1
    y := io.value2
  }

  io.outputGCD := x
  io.outputValid := y === 0.U
}

/**
 * Generate Verilog sources and save it in file GCD.v
 */
object GCD extends App {
  ChiselStage.emitSystemVerilogFile(
    new GCD,
    firtoolOpts = Array("-disable-all-randomization@@", "-strip-debug-info", "-default-layer-specialization=enable")
  )
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.