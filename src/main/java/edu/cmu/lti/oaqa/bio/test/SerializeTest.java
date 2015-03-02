package edu.cmu.lti.oaqa.bio.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.GZIPInputStream;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
//import org.apache.uima.fit.util.CasIOUtil;
import org.apache.uima.jcas.JCas;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.uimafit.factory.JCasFactory;
import org.xml.sax.SAXException;

/**
 * Created by mog on 8/15/14.
 */
public class SerializeTest {

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException, SAXException, IOException, UIMAException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://metal.lti.cs.cmu.edu:3306/oaevaluation", "ruil",
				"1<3Uima");
		Statement stmt = conn.createStatement();
		String query = "select xcas from cas_str where experiment='a0dde5e5-9a9e-4364-b848-138c44e50d23';";
		ResultSet rs = stmt.executeQuery(query);

		JCas nextCas = null;
		LobHandler lobHandler = new DefaultLobHandler();
		nextCas = JCasFactory.createJCas();
		int ind = 0;
		while (rs.next()) {
			byte[] byteArray = lobHandler.getBlobAsBytes(rs, "xcas");
			GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(
					byteArray));
			nextCas.reset();
			XmiCasDeserializer.deserialize(gz, nextCas.getCas(), true);
//			CasIOUtil.writeXmi(nextCas, new File("/tmp/example" + ind++
//					+ ".xmi"));
		}
	}

}