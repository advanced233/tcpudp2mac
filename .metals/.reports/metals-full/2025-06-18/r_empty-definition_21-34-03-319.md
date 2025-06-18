error id: file://<WORKSPACE>/src/top/test.scala:`<none>`.
file://<WORKSPACE>/src/top/test.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -chisel3/Array.
	 -chisel3/Array#
	 -chisel3/Array().
	 -chisel3/util/Array.
	 -chisel3/util/Array#
	 -chisel3/util/Array().
	 -Array.
	 -Array#
	 -Array().
	 -scala/Predef.Array.
	 -scala/Predef.Array#
	 -scala/Predef.Array().
offset: 310
uri: file://<WORKSPACE>/src/top/test.scala
text:
```scala
import chisel3._
import chisel3.util._

class test extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val c = Output(UInt(8.W))
  })

  io.c := io.a + io.b
}

object TEST extends App {
  ChiselStage.emitSystemVerilogFile(
    new test,
    firtoolOpts = A@@rray("-disable-all-randomization", "-strip-debug-info", "-default-layer-specialization=enable")
  )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.