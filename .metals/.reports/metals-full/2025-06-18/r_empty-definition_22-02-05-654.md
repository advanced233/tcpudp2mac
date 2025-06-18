error id: file://<WORKSPACE>/chisel-playground/playground/src/Elaborate.scala:emitSystemVerilogFile.
file://<WORKSPACE>/chisel-playground/playground/src/Elaborate.scala
empty definition using pc, found symbol in pc: emitSystemVerilogFile.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -circt/stage/ChiselStage.emitSystemVerilogFile.
	 -circt/stage/ChiselStage.emitSystemVerilogFile#
	 -circt/stage/ChiselStage.emitSystemVerilogFile().
	 -scala/Predef.circt.stage.ChiselStage.emitSystemVerilogFile.
	 -scala/Predef.circt.stage.ChiselStage.emitSystemVerilogFile#
	 -scala/Predef.circt.stage.ChiselStage.emitSystemVerilogFile().
offset: 369
uri: file://<WORKSPACE>/chisel-playground/playground/src/Elaborate.scala
text:
```scala
object Elaborate extends App {
  val firtoolOptions = Array(
    "--lowering-options=" + List(
      // make yosys happy
      // see https://github.com/llvm/circt/blob/main/docs/VerilogGeneration.md
      "disallowLocalVariables",
      "disallowPackedArrays",
      "locationInfoStyle=wrapInAtSquareBracket"
    ).reduce(_ + "," + _)
  )
  circt.stage.ChiselStage.emi@@tSystemVerilogFile(new gcd.GCD(), args, firtoolOptions)
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: emitSystemVerilogFile.