import org.json.simple.JsonArray;
import org.json.simple.JsonObject;

public class App {
    public static void main(String[] args) throws Exception {

        final String __invoiceRegex = "<Invoices.*\\>[\\s\\S]*?\\<\\/Invoices\\>";

        java.io.FileReader fr=new java.io.FileReader("C:\\Users\\qha\\Desktop\\EV32021000015359.XML");
        java.io.BufferedReader br=new java.io.BufferedReader(fr);
        String __xmlstr ="";
        int i;
        while((i=br.read())!=-1){
        __xmlstr +=(char)i;
        }
        br.close();
        fr.close();
        final java.util.regex.Pattern __invoicePattern = java.util.regex.Pattern.compile(__invoiceRegex, java.util.regex.Pattern.MULTILINE);
        final java.util.regex.Matcher __invoiceMatcher = __invoicePattern.matcher(__xmlstr);

        __invoiceMatcher.find();
        StringBuilder __xmlStringBuilder = new StringBuilder();
        __xmlStringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        __xmlStringBuilder.append(__invoiceMatcher.group(0));

        javax.xml.parsers.DocumentBuilderFactory __factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder __builder = __factory.newDocumentBuilder();
        java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(
        __xmlStringBuilder.toString().getBytes("UTF-8"));
        org.w3c.dom.Document __doc = __builder.parse(input);
        __doc.getDocumentElement().normalize();

        JsonObject invoiceJson = new JsonObject();
        JsonArray  invoiceLinesJsonArray = new JsonArray();
        org.w3c.dom.Element __rootElement = __doc.getDocumentElement();
        invoiceJson.addProperty("partyName",  __rootElement.getElementsByTagName("PartyName").item(1).getTextContent());
        invoiceLinesJsonArray.add(invoiceJson);
        //rpa.echo(invoiceJson);

        JsonObject invoiceJsonDate = new JsonObject();
        invoiceJsonDate.addProperty("issueDate",  __rootElement.getElementsByTagName("IssueDate").item(0).getTextContent());
        invoiceLinesJsonArray.add(invoiceJsonDate);

        JsonObject invoiceIDJson = new JsonObject();
        invoiceIDJson.addProperty("invoiceID",  __rootElement.getElementsByTagName("InvoiceID").item(0).getTextContent());
        invoiceLinesJsonArray.add(invoiceIDJson);

        JsonObject invoiceJsonPaymentDueDate = new JsonObject();
        invoiceJsonPaymentDueDate.addProperty("paymentDueDate",  __rootElement.getElementsByTagName("PaymentDueDate").item(0).getTextContent());
        invoiceLinesJsonArray.add(invoiceJsonPaymentDueDate);

        JsonObject invoiceJsonInvoiceType = new JsonObject();
        invoiceJsonInvoiceType.addProperty("invoiceTypeCode",  __rootElement.getElementsByTagName("InvoiceTypeCode").item(0).getTextContent());
        invoiceLinesJsonArray.add(invoiceJsonInvoiceType);

        JsonObject invoiceJsonTaxPrice = new JsonObject();
        invoiceJsonTaxPrice.addProperty("taxInclusiveAmount",  __rootElement.getElementsByTagName("TaxInclusiveAmount").item(0).getTextContent());
        invoiceLinesJsonArray.add(invoiceJsonTaxPrice);


        org.w3c.dom.NodeList __list = __rootElement.getElementsByTagName("LineExtensionAmount");
        for (int temp = 0; temp < __list.getLength() - 1; temp++) {
        org.w3c.dom.Node nNode = __list.item(temp);

        if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
            org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
            JsonObject __invoiceLine = new JsonObject();
            __invoiceLine.addProperty("amount", eElement.getElementsByTagName("Amount").item(0).getTextContent());

            invoiceLinesJsonArray.add(__invoiceLine);
            //rpa.echo(__invoiceLine);
        }
        }

        invoiceJson.add("InvoiceLines", invoiceLinesJsonArray);


    }
}
