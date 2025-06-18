error id: file://<WORKSPACE>/chisel-playground/playground/src/test.scala:`<none>`.
file://<WORKSPACE>/chisel-playground/playground/src/test.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -chisel3/Output.
	 -chisel3/Output#
	 -chisel3/Output().
	 -Output.
	 -Output#
	 -Output().
	 -scala/Predef.Output.
	 -scala/Predef.Output#
	 -scala/Predef.Output().
offset: 146
uri: file://<WORKSPACE>/chisel-playground/playground/src/test.scala
text:
```scala
import chisel3._

class test extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val c = Out@@put(UInt(8.W))
  })

  io.c := io.a + io.b
}

object Elaborat extends App {
  val firtoolOptions = Array(
    "--lowering-options=" + List(
      // make yosys happy
      // see https://github.com/llvm/circt/blob/main/docs/VerilogGeneration.md
      "disallowLocalVariables",
      "disallowPackedArrays",
      "locationInfoStyle=wrapInAtSquareBracket"
    ).reduce(_ + "," + _)
  )
  circt.stage.ChiselStage.emitSystemVerilogFile(new gcd.GCD(), args, firtoolOptions)
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.