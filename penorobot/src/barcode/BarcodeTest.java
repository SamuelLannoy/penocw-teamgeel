//package barcode;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lightsensor.Color;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class BarcodeTest {
//
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//	
//	@Test
//	public void testConvertBarcode_TurnLeft(){
//		List<Color> colors = new ArrayList<Color>();
//		colors.add(Color.BLACK);
//		
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.WHITE);
//		colors.add(Color.BLACK);
//		colors.add(Color.WHITE);
//
//		colors.add(Color.BLACK);
//		
//		assertEquals(BarcodeParser.TURNLEFT, BarcodeParser.convertToBarcode(colors));
//	}
//	
//	@Test
//	public void testConvertBarcode_Incomplete(){
//		List<Color> colors = new ArrayList<Color>();
//		colors.add(Color.BLACK);
//		
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//
//		colors.add(Color.BLACK);
//		
//		try{
//			BarcodeParser.convertToBarcode(colors);
//		}
//		catch(IllegalArgumentException e){
//			assertEquals(true, true);
//		}
//	}
//	
//	@Test
//	public void testConvertBarcode_Incomplete_Multiple(){
//		List<Color> colors = new ArrayList<Color>();
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		
//		
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);		
//		colors.add(Color.BLACK);
//		
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);		
//		colors.add(Color.WHITE);
//		
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		try{
//			BarcodeParser.convertToBarcode(colors);
//		}
//		catch(IllegalStateException e){
//			assertEquals(true, true);
//		}	}
//	
//	@Test
//	public void testConvertBarcode_TurnRight(){
//		List<Color> colors = new ArrayList<Color>();
//		colors.add(Color.BLACK);
//		
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.WHITE);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.WHITE);
//
//		colors.add(Color.BLACK);
//		
//		assertEquals(BarcodeParser.TURNRIGHT, BarcodeParser.convertToBarcode(colors));
//	}
//	
//	@Test
//	public void testConvertBarcode_TurnRight_Double(){
//		List<Color> colors = new ArrayList<Color>();
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//
//
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//
//
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		
//		assertEquals(BarcodeParser.TURNRIGHT, BarcodeParser.convertToBarcode(colors));
//	}
//	
//	@Test
//	public void testConvertBarcode_TurnRight_Multiple(){
//		List<Color> colors = new ArrayList<Color>();
//		// i = 0
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//
//		// i = 1
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//
//		// i = 2
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//
//		// i = 3
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE); 
//
//
//		// i = 4
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//
//		// i = 5
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//
//		// i = 5
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//
//		// i = 6
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//
//		// i = 7
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//
//		System.out.println("size: "+colors.size());
//		assertEquals(BarcodeParser.TURNRIGHT, BarcodeParser.convertToBarcode(colors));
//	}
//	
//	@Test
//	//011001
//	public void testConvertBarcode_LowSpeed_Multiple_mod8is7(){
//		List<Color> colors = new ArrayList<Color>();
//		// i = 0
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK); //10
//
//
//		// i = 1
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); //20
//
//		// i = 2
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); //30
//		colors.add(Color.WHITE); //31
//
//		// i = 3
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); //40
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE); //42
//
//
//		// i = 4
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); //50
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); //52 
//
//		// i = 5
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); //60 
//
//		// i = 6
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); //70
//		colors.add(Color.WHITE); //71
//
//
//		// i = 7
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); //78
//
//		System.out.println("size: "+colors.size());
//		System.out.println("mod: "+colors.size() % 8);
//		assertEquals(BarcodeParser.LOWSPEED, BarcodeParser.convertToBarcode(colors));
//	}
//	
//	@Test
//	//011001
//	public void testLowSpeed_Multiple_InReverse(){
//		List<Color> colors = new ArrayList<Color>();
//		// i = 0
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		
//		// i = 1
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		
//		// i = 2
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		
//		// i = 3
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		
//		// i = 4
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		
//		// i = 5
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE); 
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		colors.add(Color.WHITE);
//		
//		// i = 6
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		
//
//		//i=7
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK);
//		colors.add(Color.BLACK); 
//		colors.add(Color.BLACK);
//		
//		System.out.println("size: "+colors.size());
//		System.out.println("mod: "+colors.size() % 8);
//		assertEquals(BarcodeParser.LOWSPEED, BarcodeParser.convertToBarcode(colors));
//		
//	}
//
//}
