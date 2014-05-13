name := "sortx"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "org.scalafx"     %% "scalafx"    % "8.0.0-R4",
  "org.docx4j"      % "docx4j"      % "3.1.0",
  "org.controlsfx"  % "controlsfx"  % "8.0.5"
)

packageArchetype.java_application
