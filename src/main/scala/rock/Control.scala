package rockchip.rock

import chisel3._
import chisel3.util.{BitPat, Fill, Cat, Reverse}
import org.chipsalliance.cde.config.Parameters

/* to pc, icache, branch, csr, alu, load, store, wb */
class ControlIO(implicit p:Parameters) extends CoreBundle() (p) {
    val inst = Input(UInt(xlen.W))    /* instruction */
    val pc_sel = Output(UInt(2, W))   /* pc */
    val inst_kill = Output(Bool())
    val A_sel = Output(UInt(1.W))
    val B_sel = Output(UInt(1.W))
    val Imm_sel = Output(UInt(3.W))
    val alu_op = Output(UInt(4.W))
    val br_type = Output(UInt(3.W))
    val st_type = Output(UInt(2.W))
    val ld_type = Output(UInt(3.W))
    val wb_type = Output(UInt(2.W))
    val wb_en = Output(Bool())
    val csr_cmd = Output(UInt(3.W))
    val illegal = Output(Bool())
}


abstract class AbstractControl(implicit p:Parameters) extends CoreBundle() (p) {
    val io = IO(new ControlIO)
}

class Control(implicit val p:Parameters) extends AbstractControl() (p) with CoreParams{
}