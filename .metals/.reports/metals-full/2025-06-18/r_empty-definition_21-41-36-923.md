error id: file://<WORKSPACE>/src/top/tes.scala:`<none>`.
file://<WORKSPACE>/src/top/tes.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 105
uri: file://<WORKSPACE>/src/top/tes.scala
text:
```scala
package top
import chisel3._
import chisel3.util._
import _root_.circt.stage.ChiselStage

class tes exten@@ds Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val c = Output(UInt(8.W))
  })

  io.c := io.a + io.b
}

object TES extends App {
  ChiselStage.emitSystemVerilogFile(
    new tes,
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info", "-default-layer-specialization=enable")
  )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.