module CSE360TeamProject {
	requires javafx.controls;
	requires java.sql;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
