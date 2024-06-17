package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

   private static final String CUIT = "";

   private static final String CLAVE_FISCAL = "";

   private static final String RAZON_SOCIAL = "GERBER PABLO CAMILO";

   public static final String DESCRIPCION_FACTURA = "Servicio de consultoria de software";

   //MAXIMO MONTO PARA CONSUMIDOR FINAL
   public static final BigDecimal MONTO_FACTURA = BigDecimal.valueOf(95000.00);

   public static final BigDecimal MONTO_MENSUAL = BigDecimal.valueOf(800000.00);

   public static void main(String[] args) {
      WebDriver driver = new ChromeDriver();

      int cantidadFacturas = MONTO_MENSUAL.divide(MONTO_FACTURA, RoundingMode.DOWN).intValue();
      try {
         // Navegar a la página web
         driver.get("https://auth.afip.gov.ar/contribuyente_/login.xhtml?action=SYSTEM&system=admin_mono");

         // Encontrar un campo de texto por su nombre y rellenarlo
         WebElement cuitForm = driver.findElement(By.id("F1:username"));
         cuitForm.sendKeys(CUIT);

         // Encontrar un botón por su id y hacer clic en él
         WebElement submitButton = driver.findElement(By.id("F1:btnSiguiente"));
         submitButton.click();

         WebElement passwordForm = driver.findElement(By.id("F1:password"));
         passwordForm.sendKeys(CLAVE_FISCAL);

         WebElement passwordSubmitButton = driver.findElement(By.id("F1:btnIngresar"));
         passwordSubmitButton.click();

         Thread.sleep(3000);

         WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
         WebElement link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li/a[contains(text(),'Facturación')]")));

         link.click();

         WebElement generateInvoiceButton = driver.findElement(By.id("bBtn1"));
         generateInvoiceButton.click();

         ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

         // Cambiar al nuevo tab
         driver.switchTo().window(tabs.get(1));

         WebElement companyNameButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='" + RAZON_SOCIAL + "']")));
         companyNameButton.click();

         for (int c = 0; c < cantidadFacturas; c++) {
            WebElement generateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='btn_gen_cmp']/span[2]")));
            generateButton.click();

            if (c == 0) {
               WebElement closeModalButton = driver.findElement(By.id("novolveramostrar"));
               closeModalButton.click();
            }

            WebElement puntoDeVenta = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/div/div/table/tbody/tr[1]/td/select")));
            Select select = new Select(puntoDeVenta);
            select.selectByIndex(2);

            Thread.sleep(2000);

            WebElement continuarButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Continuar >']")));
            continuarButton.click();

            WebElement conceptosAIncluir = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/div/div/table/tbody/tr[2]/td/select")));
            Select conceptoSelect = new Select(conceptosAIncluir);
            conceptoSelect.selectByIndex(2);

            WebElement actividad = wait.until(ExpectedConditions.elementToBeClickable(
                  By.xpath("/html/body/div[2]/form/div/div/table/tbody/tr[6]/td/table/tbody/tr[2]/td/table/tbody/tr[2]/th/select")));
            Select actividadSelect = new Select(actividad);
            actividadSelect.selectByIndex(1);

            WebElement continuarButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='contenido']/form/input[2]")));
            continuarButton2.click();

            WebElement condicionIva = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/div/div/table[1]/tbody/tr[1]/td/select")));
            Select condicionIvaSelect = new Select(condicionIva);
            condicionIvaSelect.selectByValue("5");

            WebElement tipoDoc = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/div/div/table[1]/tbody/tr[2]/td/select")));
            Select tipoDocSelect = new Select(tipoDoc);
            tipoDocSelect.selectByIndex(0);

            WebElement labelElement = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/form/div/div/table[2]/tbody/tr[8]/th/label")));
            labelElement.click();

            WebElement continuarButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='formulario']/input[2]")));
            continuarButton3.click();

            WebElement description = driver.findElement(By.id("detalle_descripcion1"));
            description.sendKeys(DESCRIPCION_FACTURA);

            WebElement amount = driver.findElement(By.id("detalle_precio1"));
            amount.sendKeys(MONTO_FACTURA.setScale(2, RoundingMode.DOWN).toString());

            WebElement continuarButton4 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='contenido']/form/input[8]")));
            continuarButton4.click();

            WebElement triggerElement = driver.findElement(By.id("btngenerar"));
            triggerElement.click();

            Alert alert = driver.switchTo().alert();
            alert.accept();

            WebElement menuPrincipal = wait.until(
                  ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='contenido']/table/tbody/tr[2]/td/input")));
            menuPrincipal.click();

         }

      } catch (InterruptedException e) {
         e.printStackTrace();
      } finally {
         // Cerrar el navegador
         //driver.quit();
      }
   }

}