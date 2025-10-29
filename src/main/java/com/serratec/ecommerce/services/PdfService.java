package com.serratec.ecommerce.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.serratec.ecommerce.dtos.NotaFiscalDTO;
import com.serratec.ecommerce.dtos.NotaFiscalItemDTO;
import com.serratec.ecommerce.dtos.NotaFiscalPedidoDTO;

@Service
public class PdfService {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public byte[] gerarPdfNotaFiscal(NotaFiscalDTO nota) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		Paragraph titulo = new Paragraph("NOTA FISCAL").setBold().setFontSize(18)
				.setTextAlignment(TextAlignment.CENTER);
		document.add(titulo);

		document.add(new Paragraph("Cliente: " + nota.getNomeCliente()).setFontSize(12));
		document.add(new Paragraph("CPF: " + nota.getCpfCliente()).setFontSize(12));
		document.add(new Paragraph("E-mail: " + nota.getEmailCliente()).setFontSize(12));
		document.add(new Paragraph("--------------------------------------------------").setBold());

		NotaFiscalPedidoDTO pedido = nota.getPedido();

		document.add(new Paragraph("Pedido ID: " + pedido.getPedidoId()).setBold());
		document.add(new Paragraph("Data: " + pedido.getDataPedido().format(FORMATTER)));
		document.add(new Paragraph("Status: " + pedido.getStatusPedido()));

		Table tabelaItens = new Table(UnitValue.createPercentArray(new float[] { 4, 1, 2, 2, 2 }));
		tabelaItens.setWidth(UnitValue.createPercentValue(100)).setMarginTop(10);

		tabelaItens.addHeaderCell(new Cell().add(new Paragraph("Produto").setBold()));
		tabelaItens.addHeaderCell(new Cell().add(new Paragraph("Qtd.").setBold()));
		tabelaItens.addHeaderCell(new Cell().add(new Paragraph("Vl. Unit.").setBold()));
		tabelaItens.addHeaderCell(new Cell().add(new Paragraph("Desconto").setBold()));
		tabelaItens.addHeaderCell(new Cell().add(new Paragraph("Subtotal").setBold()));

		for (NotaFiscalItemDTO item : pedido.getItens()) {
			tabelaItens.addCell(item.getNomeProduto());
			tabelaItens.addCell(String.valueOf(item.getQuantidade()));
			tabelaItens.addCell("R$ " + item.getValorUnitario());
			tabelaItens.addCell("R$ " + item.getDesconto());
			tabelaItens.addCell("R$ " + item.getSubtotal());
		}
		document.add(tabelaItens);

		Paragraph totalPedido = new Paragraph("Total do Pedido: R$ " + pedido.getValorTotalPedido()).setBold()
				.setFontSize(14).setTextAlignment(TextAlignment.RIGHT).setMarginBottom(20);
		document.add(totalPedido);

		document.close();
		return baos.toByteArray();
	}

}
