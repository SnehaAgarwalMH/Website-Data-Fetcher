package com.AutoExcelProject.OpenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.Keys;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class Test1 {
		
		private static WebDriver driver;
		private static WebDriverWait wait;
		
		public static void main(String[] args) {

			System.setProperty("webdriver.chrome.driver", "My_Path\\chromedriver.exe");
			System.setProperty("webdriver.chrome.logfile", "My_Path\\chromedriver.log");
			System.setProperty("webdriver.chrome.verboseLogging", "true");

			
			// Set up ChromeDriver logging and options		
			    ChromeOptions options = new ChromeOptions();
			    options.addArguments("--remote-debugging-port=9222");
			    driver = new ChromeDriver(options);		      
			    driver.manage().window().fullscreen();
			    wait = new WebDriverWait(driver, Duration.ofSeconds(20));  


		    // Load the Excel file
		    String excelFilePath = "C:\\Users\\sneha\\Documents\\work.xlsx";
		    try (FileInputStream fis = new FileInputStream(excelFilePath);
		    		Workbook workbook = new XSSFWorkbook(fis)) {

		            // Assuming the data is present in the first sheet (Sheet index 0)Address		    	

		            Sheet sheet = workbook.getSheetAt(0);

		         // Create a mapping of website URLs to their corresponding method names
		            Map<String, String> websiteHandlers = createWebsiteHandlers(sheet);

		            // Start from the second row (index 1) to skip the header row
		            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
		                Row row = sheet.getRow(rowIndex);

		                // Get the values from the "address" and "website_link" columns
		                String address = getCellValue(row, "Address");
		                String websiteLink = getCellValue(row, "Website_Link");
		                String methodHandler = getCellValue(row, "Network");
		                String bname = getCellValue(row, "Type"); // Extract value from the "MethodName_Column" column
			            String combinedMethodName = methodHandler + "_" + bname; // 
		                
		                // Navigate to the website
		                driver.get(websiteLink);

		                // Call the website-specific method to perform the action
		                String methodName = websiteHandlers.get(combinedMethodName);
		                if (methodName != null) {
		                    performAction(driver, methodName, address);
		                } else {
		                    System.out.println("No handler found for website: " + websiteLink);
		                }
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            // Quit the driver after the loop is complete
		            driver.quit();
		        }
		    }

		    private static Map<String, Integer> createColumnMapping(Row headerRow) {
		        Map<String, Integer> columnMapping = new HashMap<>();
		        for (Cell cell : headerRow) {
		            String columnName = cell.getStringCellValue();
		            int columnIndex = cell.getColumnIndex();
		            columnMapping.put(columnName, columnIndex);
		        }
		        return columnMapping;
		    }

		    private static String getCellValue(Row row, String columnName) {
		        Sheet sheet = row.getSheet();
		        Map<String, Integer> columnMapping = createColumnMapping(sheet.getRow(0));
		        int columnIndex = columnMapping.get(columnName);
		        Cell cell = row.getCell(columnIndex);
		        if (cell == null || cell.getCellType() == CellType.BLANK) {
		            return "";
		        }
		        return cell.getStringCellValue();
		    }

		    private static Map<String, String> createWebsiteHandlers(Sheet sheet) {
		        Map<String, String> websiteHandlers = new HashMap<>();
		        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
		            Row row = sheet.getRow(rowIndex);
		            String websiteLink = getCellValue(row, "Website_Link");
		            String methodHandler = getCellValue(row, "Network");
		            String bname = getCellValue(row, "Type"); // Extract value from the "MethodName_Column" column
		            String combinedMethodName = methodHandler + "_" + bname; // 
		            websiteHandlers.put(websiteLink, combinedMethodName);
		        }
		        return websiteHandlers;
		    }

		    private static void performAction(WebDriver driver, String combinedMethodName, String address) {
		    	
		        try {
		        	
		        	// Extract the actual method name from the combinedMethodName (e.g., "BTC_address" -> "BTC")
		            String methodName = combinedMethodName.split("_")[0];
		            
		         // Get the method object based on the method name
		            Method method = Test1.class.getDeclaredMethod(methodName, WebDriver.class, String.class, String.class);

		            // Invoke the method with the required arguments
		            method.invoke(null, driver, address);
		        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
		            e.printStackTrace();
		        }
		    }
		    
		    private static void BCH(WebDriver driver, String combinedMethodName, String address ) throws IOException, InterruptedException {
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"__next\"]/header/div/div[2]/div/div/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	    		    	
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			            // Wait for a moment to load the content
			            Thread.sleep(4000);  
			            
			         // Take a screenshot of the current visible part of the page
			            File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			            BufferedImage fullScreen = ImageIO.read(screenshotFile);
			            ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			            // Wait for a moment to load the content
			            Thread.sleep(2000); 
		    }
		    
		    private static void BTC(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"__next\"]/header/div/div[2]/div/div/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	WebElement link = driver.findElement(By.xpath("//*[@id=\"__next\"]/div[1]/div/div/div[1]/div/div[2]/div/div[2]/a"));
		    	link.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	handleCookieConsent();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			            // Wait for a moment to load the content
			            Thread.sleep(4000);  
			            
			            // Take a screenshot of the current visible part of the page
			            File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			            BufferedImage fullScreen = ImageIO.read(screenshotFile);
			            ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			            // Wait for a moment to load the content
			            Thread.sleep(2000); 
		    }
		    
		    private static void handleCookieConsent() {
		        // Check if the cookie consent pop-up is present
		        WebElement cookiePopup = null;
		        try {
		        	cookiePopup = driver.findElement(By.className("main-wrapper")); //cookie pop-up element
		        } catch (NoSuchElementException e) {
		            // If the element is not found, no cookie pop-up is present
		        }

		        // Handle the cookie consent pop-up if present
		        if (cookiePopup != null) {
		            // Click on the "Accept" button
		            WebElement checkBox = cookiePopup.findElement(By.className("mark")); //"Accept" button
		            boolean isSelected = checkBox.isSelected();
		    		if (isSelected == false) {
		    			checkBox.click();
		    		}
		        }
		    }
		    
		    private static void BSC(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {
		    	
		    	Actions actions = new Actions(driver);
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"txtSearchInput\"]"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	WebElement enter = driver.findElement(By.className("btn btn-primary"));
		    	enter.click();
		       
		    	   // Wait for the drop-down to appear
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        By dropDownItemsLocator = By.xpath("//*[@id=\"availableBalanceDropdown\"]");
		        wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));

		        // Get all the drop-down elements
		        java.util.List<WebElement> dropDownItems = driver.findElements(dropDownItemsLocator);

		        // Take a screenshot of each drop-down item
		        for (int i = 0; i < dropDownItems.size(); i++) {
		            WebElement item = dropDownItems.get(i);
		            actions.moveToElement(item).perform();
		            item.click();

		            // Wait for a moment to load the content
		            Thread.sleep(2000);

		            // Take a screenshot of the current visible part of the page
		            Date date = new Date();
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			        String datetime = dateFormat.format(date);	
			        // Take a screenshot of the current visible part of the page
		            File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
		            BufferedImage fullScreen = ImageIO.read(screenshotFile);
		            ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + i + "_" + datetime + ".png"));

		            // Go back to the page
		            driver.navigate().back();

		            // Wait for a moment to load the page
		            Thread.sleep(2000);

		            // Wait for the drop-down to appear again after navigating back
		            wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));
		            dropDownItems = driver.findElements(dropDownItemsLocator);            
			           
		        }
		    }
		    
		    private static void BTG(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"search\"]/div[1]/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	WebElement enter = driver.findElement(By.xpath("//*[@id=\"search\"]/div[2]/button/span/svg"));
		    	enter.click();
		    	
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void DASH(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {
		    	
		    	WebElement search = driver.findElement(By.id("searchField"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	WebElement enter = driver.findElement(By.className("input-group-btn"));
		    	enter.click();
		    	
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void EOS(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {
		    	
		    	WebElement close = driver.findElement(By.className("close-button"));
		    	close.click();
		    	
		    	WebElement search = driver.findElement(By.className("search"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	WebElement enter = driver.findElement(By.id("search-button"));
		    	enter.click();
		    	
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void ETC(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("autoComplete_wrapper"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void ETH(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	Actions actions = new Actions(driver);
		    	
		    	WebElement search = driver.findElement(By.id("search-panel"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	WebElement enter = driver.findElement(By.xpath("//*[@id=\"content\"]/section[1]/div/div/div[1]/form/div/div[3]/button"));
		    	enter.click();
		    	
		        
		    	   // Wait for the drop-down to appear
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        By dropDownItemsLocator = By.id("dropdownMenuBalance");
		        wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));

		        // Get all the drop-down elements
		        java.util.List<WebElement> dropDownItems = driver.findElements(dropDownItemsLocator);

		        // Take a screenshot of each drop-down item
		        for (int i = 0; i < dropDownItems.size(); i++) {
		            WebElement item = dropDownItems.get(i);
		            actions.moveToElement(item).perform();
		            item.click();

		            // Wait for a moment to load the content
		            Thread.sleep(2000);

		            // Take a screenshot of the current visible part of the page
		            Date date = new Date();
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			        String datetime = dateFormat.format(date);	
			        // Take a screenshot of the current visible part of the page
		            File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
		            BufferedImage fullScreen = ImageIO.read(screenshotFile);
		            ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + i + "_" + datetime + ".png"));

		            // Go back to the page
		            driver.navigate().back();

		            // Wait for a moment to load the page
		            Thread.sleep(2000);

		            // Wait for the drop-down to appear again after navigating back
		            wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));
		            dropDownItems = driver.findElements(dropDownItemsLocator);            
			           
		        }
		    }
		    
		    private static void ETHF(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("index_innerInput__ScDA8"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		    	 wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		    	 
		    	// Switch to the new window
		    	String originalWindowHandle = driver.getWindowHandle();
		    	for (String windowHandle : driver.getWindowHandles()) {
		    		if (!windowHandle.equals(originalWindowHandle)) {
		    			driver.switchTo().window(windowHandle);
		    			break;
		    		}
		    		}
		    	 
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			    
			    // Close the new window and switch back to the original window
			    driver.close();
			    driver.switchTo().window(originalWindowHandle);

			    // Wait for a moment to load the content on the original page
			    Thread.sleep(2000);
		    }
		    
		    private static void ETHW(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("index_innerInput__ScDA8"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		    	 wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		    	 
		    	// Switch to the new window
		    	String originalWindowHandle = driver.getWindowHandle();
		    	for (String windowHandle : driver.getWindowHandles()) {
		    		if (!windowHandle.equals(originalWindowHandle)) {
		    			driver.switchTo().window(windowHandle);
		    			break;
		    		}
		    		}
		    	 
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			    
			    // Close the new window and switch back to the original window
			    driver.close();
			    driver.switchTo().window(originalWindowHandle);

			    // Wait for a moment to load the content on the original page
			    Thread.sleep(2000);
		    }
		    
		    private static void FIL(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("el-input__inner"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void HDAC(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.id("search"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement next = driver.findElement(By.id("searchbutton"));
		    	next.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void KLAY(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("Input__form MainPage__searchForm"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement next = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[3]/div[2]/div/div/div[1]/div/button"));
		    	next.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void LTC(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"search\"]/div[1]/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement next = driver.findElement(By.className("control"));
		    	next.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void QTUM(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"search\"]/div[1]/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement next = driver.findElement(By.className("control"));
		    	next.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void SOL(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	Actions actions = new Actions(driver);
		    	
		    	WebElement search = driver.findElement(By.className("ant-input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	
		    	WebElement enter = driver.findElement(By.xpath("//*[@id=\"root\"]/section/div/div/div[2]/div/div/form/button"));
		    	enter.click();
		    	
		        
		    	   // Wait for the drop-down to appear
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		        By dropDownItemsLocator = By.xpath("//*[@id=\"root\"]/section/main/div/div[2]/div/div[1]/div/div[2]/div[3]/span");
		        wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));

		        // Get all the drop-down elements
		        java.util.List<WebElement> dropDownItems = driver.findElements(dropDownItemsLocator);

		        // Take a screenshot of each drop-down item
		        for (int i = 0; i < dropDownItems.size(); i++) {
		            WebElement item = dropDownItems.get(i);
		            actions.moveToElement(item).perform();
		            item.click();

		            // Wait for a moment to load the content
		            Thread.sleep(2000);

		            // Take a screenshot of the current visible part of the page
		            Date date = new Date();
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			        String datetime = dateFormat.format(date);	
			        // Take a screenshot of the current visible part of the page
		            File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
		            BufferedImage fullScreen = ImageIO.read(screenshotFile);
		            ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + i + "_" + datetime + ".png"));

		            // Go back to the page
		            driver.navigate().back();

		            // Wait for a moment to load the page
		            Thread.sleep(2000);

		            // Wait for the drop-down to appear again after navigating back
		            wait.until(ExpectedConditions.visibilityOfElementLocated(dropDownItemsLocator));
		            dropDownItems = driver.findElements(dropDownItemsLocator);            
			           
		        }
		    }
		    
		    private static void TRX(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	 
		    	
		    	 Actions actions = new Actions(driver);
		    	
		    	WebElement search = driver.findElement(By.xpath("//*[@id=\"navSearchbarId\"]/div/div/div/div/div/div/section/span/span/span[1]/input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement more = driver.findElement(By.className("ant-tabs-content-more-link"));
		    	more.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + "_top.png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
			    
			    // Scroll down to capture the bottom part of the first page
	            actions.sendKeys(Keys.END).perform();
	            Thread.sleep(2000);
	            
	            // Take a screenshot of the current visible part of the page
			    screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			     fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + "_bottom.png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
		    
		    private static void XRP(WebDriver driver, String address, String combinedMethodName) throws IOException, InterruptedException {	    		    	
		    	
		    	WebElement search = driver.findElement(By.className("react-autosuggest__input"));
		    	search.click();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    	
		    	search.clear(); // Clear any existing text in the search box
		    	search.sendKeys(address);      
		    	search.sendKeys(Keys.ENTER);

		        // Submit the user name
		    	search.submit();
		    	try {
		    		Thread.sleep(2000); // wait for 2 seconds
		    	} catch (InterruptedException e) {
		    		e.printStackTrace();
		    	}	
		    	

		    	WebElement next = driver.findElement(By.xpath("//*[@id=\"root\"]/div/header/div/nav/form/button"));
		    	next.click();
		        
		        Date date = new Date();
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		        String datetime = dateFormat.format(date);  	        
			        		       
			    // Wait for a moment to load the content
			    Thread.sleep(4000);  
			            
			    // Take a screenshot of the current visible part of the page
			    File screenshotFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
			    BufferedImage fullScreen = ImageIO.read(screenshotFile);
			    ImageIO.write(fullScreen, "png", new File("C:\\Users\\sneha\\Documents\\ExcelLink\\screenshot_page_" + combinedMethodName + "_" + datetime + ".png"));
			
			    // Wait for a moment to load the content
			    Thread.sleep(2000); 
		    }
	}
