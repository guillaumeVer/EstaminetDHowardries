package hei.projet.EsaminetDHowardries.Dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hei.projet.EstaminetDHowardries.dao.DataSourceProvider;
import hei.projet.EstaminetDHowardries.dao.UtilisateurDao;
import hei.projet.EstaminetDHowardries.entite.Utilisateur;



public class UtilisateurDaoTestCase {

	UtilisateurDao utilisateurDao = new UtilisateurDao();
	@Before
	public void initBdd() throws Exception {
		Connection connection = DataSourceProvider.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM utilisateur");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`IdUtilisateur`, `Nom`, `Prenom`, `Mail`, `password`) VALUES (1,'Gg','vg','guillaume.verjot@hei.fr','gg')");
		stmt.executeUpdate("INSERT INTO `utilisateur`(`IdUtilisateur`, `Nom`, `Prenom`, `Mail`, `password`) VALUES (2,'guy','vg','verjotg@gmail.com','gg')");
		stmt.close();
		connection.close();
	}
	
	@Test
	public void testergetUnUtilisateurbyMail(){
		Utilisateur user=utilisateurDao.getUnUtilisateurbyNom("Gg");
		Assert.assertEquals("vg", user.getPrenom());
		Assert.assertEquals("guillaume.verjot@hei.fr", user.getMail());
	}
	
	@Test
	public void testerListerHoraire() {
		List<Utilisateur> lstuser = utilisateurDao.listerUtilisateur();
		Assert.assertEquals(2, lstuser.size());
		Assert.assertEquals("gg", lstuser.get(0).getPassword());
		Assert.assertEquals("guillaume.verjot@hei.fr", lstuser.get(0).getMail());
	
		}

}