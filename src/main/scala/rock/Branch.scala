package rockchip.rock

import chisel3._
import chisel3.util.{BitPat, Fill, Cat, Reverse}
import org.chipsalliance.cde.config.Parameters

class BranchIO(implicit p:Parameters) extends CoreBundle() (p) {
    val rs1 = Input(UInt(xlen.W))
    val rs2 = Input(UInt(xlen.W))
    val bt_type = Input(UInt(3.W))
    val taken = Output(Bool())
}


abstract class Branch(implicit p:Parameters) extends CoreBundle() (p) {
    val io = IO(new BranchIO)
}

class BranchArea(implicit val p:Parameters) extends Branch() (p) with CoreParams{
    val diff = io.rs1 - io.rs2
    val neq = diff.orR
    val eq = !neq
    val isSameSign = io.rs1(xlen-1) === io.rs2(xlen-1)
    val lt = Mux(isSameSign, diff(xlen-1), io.rs1(xlen-1))
    val ltu = Mux(isSameSign, diff(xlen-1), io.rs2(xlen-1))
    val ge = !lt
    val geu = !ltu

    io.taken :=
        ((io.br_type === BR_EQ) && eq ||
         (io.br_type === BR_NE) && neq ||
         (io.br_type === BR_LT) && lt ||
         (io.br_type === BR_LTU) && ltu ||
         (io.br_type === BR_ge) && ge ||
         (io.br_type === BR_geu) && geu)
} 