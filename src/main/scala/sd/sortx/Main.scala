package sd.sortx

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.control.{Button, TextArea}
import scalafx.scene.layout.VBox
import scalafx.stage.FileChooser
import java.io.File
import scalafx.scene.input.MouseEvent
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.controlsfx.dialog.Dialogs
import org.docx4j.jaxb.Context

object Main extends JFXApp {
  val txtInput = new TextArea()
  @inline def input = txtInput.text.getValueSafe
  def sorted = input.split("\n|,|;").map(_.trim).sorted
  def redP(tuple: (String, Boolean)) = {
    val (s, red) = tuple
    val factory = Context.getWmlObjectFactory //factory is used to create docx object like text, color, paragraph,..
    val t = factory.createText; t.setValue(s)

    val run = factory.createR; run.getContent.add(t)

    if(red) {
      val color = factory.createColor; color.setVal("FF0000")
      val rpr = factory.createRPr; rpr.setColor(color)
      run.setRPr(rpr)
    }

    val p = factory.createP; p.getContent.add(run)

    p
  }

  def save(f: File) = {
    val wordMLPackage = WordprocessingMLPackage.createPackage()
    val mdp = wordMLPackage.getMainDocumentPart
    val texts = sorted
    val reds = texts.indices.map { i =>
      i < texts.length - 1 && texts(i) == texts(i + 1) ||
      i > 0 && texts(i - 1) == texts(i)
    }
    texts zip reds map redP foreach mdp.addObject

    wordMLPackage.save(f)
  }

  val btnOK = new Button("OK"){
    onMouseClicked = {e: MouseEvent =>
      if(input == ""){
        Dialogs.create().owner(stage)
          .message("Hãy nhập dữ liệu vào ô bên trên!")
          .showWarning()
      }else {
        val fileChooser = new FileChooser()
        fileChooser.initialDirectory = new File(System.getProperty("user.home"))
        fileChooser.initialFileName = "sorted.docx"
        val f = fileChooser.showSaveDialog(stage)
        if (f != null) save(f)
      }
    }
  }

  def createContent = {
    val vbox = new VBox()
    vbox.children.addAll(txtInput, btnOK)
    vbox
  }
  
  stage = new JFXApp.PrimaryStage {
    title = "Hello World"
    width = 536
    height = 192
    scene = new Scene {
      fill = Color.LIGHTGREEN
      content = Set(createContent)
    }
  }
}
