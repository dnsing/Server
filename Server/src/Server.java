import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Server {
	private static ServerSocket serversocket;
	static ObjectMapper mapper = new ObjectMapper();
	static XmlMapper xmlma = new XmlMapper();
	public static String recieved2;
	public static int i=1;

	public static void server() throws IOException, SAXException, ParserConfigurationException {
		serversocket = new ServerSocket(1133,10);
		System.out.println("Contacts server is ready ....");

		//Server
		while (true) {
			Socket in = serversocket.accept();
			InputStream entrada = in.getInputStream();

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {
				br = new BufferedReader(new InputStreamReader(entrada));
				line = br.readLine();
				sb.append(line);
				while(line.equals("</song>")!=true) {
					line = br.readLine();
					sb.append(line);
					if (line.contains("</song>")){
						break;
					}else if (line.contains("</password>")){	
						break;
					}
				}

			}catch(IOException e){
				e.printStackTrace();
			}

			sb.toString();
			String recieved = new String(sb);
			String recieved1 = recieved.trim();
			recieved2 = recieved1.substring(21);
			recieved2 += "</XML>";
			//System.out.println(recieved2);

			//agarra el XML del cliente y lo descomprime
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(recieved2)));
			NodeList ListaNodos = doc.getElementsByTagName("opcode");
			Node node = ListaNodos.item(0);

			if(node.getTextContent().equals("0")) {
				op0();
				System.out.println(i);
			} else if(node.getTextContent().equals("1")) {
				op1();
			}


		}
	}

	public static  void op0() throws IOException, ParserConfigurationException, SAXException {
		while (true) {
			Socket in = serversocket.accept();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(recieved2)));
			NodeList ListaNodos = doc.getElementsByTagName("cancionNombre");
			Node nombre = ListaNodos.item(0);
			NodeList ListaNodos1 = doc.getElementsByTagName("artista");
			Node artista = ListaNodos1.item(0);
			NodeList ListaNodos2 = doc.getElementsByTagName("album");
			Node album = ListaNodos2.item(0);
			NodeList ListaNodos3 = doc.getElementsByTagName("path");
			Node path = ListaNodos3.item(0);


			//Creacion del Json
			Song[] song = new Song[i]; 
			song[i-1] = new Song();
			song[i-1].setNombre(nombre.getTextContent());
			song[i-1].setArtista(artista.getTextContent());
			song[i-1].setAlbum(album.getTextContent());
			song[i-1].setPath(path.getTextContent());

			File file = new File("Song"+i+".json");
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, song);

			PrintWriter pw = new PrintWriter(in.getOutputStream(), true);
			pw.println(recieved2);
			i ++;
			in.close();
			break;
		}
	}
	public static void op1() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();

		Document doc = builder.parse(new InputSource(new StringReader(recieved2)));
		NodeList ListaNodos = doc.getElementsByTagName("username");
		Node username = ListaNodos.item(0);
		NodeList ListaNodos1 = doc.getElementsByTagName("password");
		Node password = ListaNodos1.item(0);

		//Creacion del Json
		LogIn[] log = new LogIn[1]; 
		log[0] = new LogIn();
		log[0].setUser(username.getTextContent());
		log[0].setPass(password.getTextContent());

		File file = new File("LogIn.json");
		mapper.writerWithDefaultPrettyPrinter().writeValue(file, log);


	}
	public static void main(String[] args) throws Exception {
		server();
	}
}