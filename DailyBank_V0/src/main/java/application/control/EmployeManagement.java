package application.control;

import java.util.ArrayList;
import java.util.List;

import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class EmployeManagement {

    public List<Employe> getAllEmployes() {
        List<Employe> employes = new ArrayList<>();

        try {
            Access_BD_Employe accessEmploye = new Access_BD_Employe();
            employes = accessEmploye.getAllEmployes();
        } catch (DatabaseConnexionException e) {
            e.printStackTrace();
        } catch (ApplicationException ae) {
            ae.printStackTrace();
        }

        return employes;
    }
}