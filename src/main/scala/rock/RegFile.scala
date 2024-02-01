package rockchip.rock

import chisel3._
import chisel3.util.{BitPat, Fill, Cat, Reverse}
import org.chipsalliance.cde.config.Parameters

class RegFileIO(implicit p:Parameters) extends CoreBundle() (p) {
    val raddr1 = Input(UInt(5.W))
    val raddr2 = Input(UInt(5.W))
    val rdata1 = Input(UInt(xlen.W))
    val rdata2 = Input(UInt(xlen.W))

    val wen = Output(Bool())
    val waddr = Input(UInt(5.W))
    val wdata = Input(UInt(xlen.W))
}


abstract class Regfile(implicit p:Parameters) extends CoreBundle() (p) {
    val io = IO(new RegFileIO)
}

class RegFile(implicit val p:Parameters) extends Module() (p) with CoreParams{
    val io = IO(new RegFileIO)
    val regs = Mem(32, Uint(xlen.W))
    io.rdata1 = Mux(io.raddr1, regs(io.raddr1), 0.U)
    io.rdata2 = Mux(io.raddr2, regs(io.raddr2), 0.U)

    when(io.wen & io.waddr.orR) {
        regs(io.waddr) := io.wdata
    }
}