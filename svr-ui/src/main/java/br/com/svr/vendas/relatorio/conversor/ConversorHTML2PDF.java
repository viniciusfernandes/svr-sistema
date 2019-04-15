package br.com.svr.vendas.relatorio.conversor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import br.com.svr.vendas.relatorio.conversor.exception.ConversaoHTML2PDFException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

final class ConversorHTML2PDF {

    private Charset charset;

    public ConversorHTML2PDF() {

    }

    public ConversorHTML2PDF(Charset charset) {
        this.charset = charset;
    }

    public byte[] converter(InputStream arquivoHTML, int largura, int altura) throws ConversaoHTML2PDFException {
        final Document document = new Document();
        document.setPageSize(new Rectangle(largura, altura));
        document.setMargins(15, 15, 15, 15);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, outputStream);
            writer.setLanguage(Locale.getDefault().toString());
        } catch (DocumentException e) {
            throw new ConversaoHTML2PDFException("N�o foi possivel gerar o outputstream do arquivo PDF", e);
        }
        document.open();
        try {

            if (this.charset == null) {
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, arquivoHTML);
            } else {
                XMLWorkerHelper.getInstance().parseXHtml(writer, document, arquivoHTML, charset);
            }

        } catch (IOException e) {
            throw new ConversaoHTML2PDFException(
                    "N�o foi possivel parsear o conteudo de HTML para formato PDF do arquivo", e);
        }

        document.close();
        writer.close();

        final byte[] conteudo = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new ConversaoHTML2PDFException("Falha ao finalizar a geracao do arquivo PDF. "
                    + "Nao foi possivel fechar o arquivo apos a leitura.", e);
        }
        return conteudo;
    }
}
