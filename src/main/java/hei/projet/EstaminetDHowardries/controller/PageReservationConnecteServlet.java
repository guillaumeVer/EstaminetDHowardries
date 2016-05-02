package hei.projet.EstaminetDHowardries.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hei.projet.EstaminetDHowardries.dao.SendTextMessage;
import hei.projet.EstaminetDHowardries.entite.Horaire;
import hei.projet.EstaminetDHowardries.entite.Reservation;
import hei.projet.EstaminetDHowardries.entite.Table;
import hei.projet.EstaminetDHowardries.entite.Utilisateur;
import hei.projet.EstaminetDHowardries.manager.HoraireManager;
import hei.projet.EstaminetDHowardries.manager.ReservationManager;
import hei.projet.EstaminetDHowardries.manager.TableManager;
import hei.projet.EstaminetDHowardries.manager.UtilisateurManager;

@WebServlet("/prive/Reservation")
public class PageReservationConnecteServlet extends HttpServlet {


	private static final long serialVersionUID = -159275287852148698L;

	private Utilisateur user;
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		req.setAttribute("user",user);
	
		
		List<Horaire> lstHoraire = HoraireManager.getInstance().listerHoraire();
		req.setAttribute("listedHoraires", lstHoraire);
		
		List<Table> lstTable = TableManager.getInstance().listerTable();
		req.setAttribute("listeDeTable",lstTable);
		
		List<Reservation> lstReservation = ReservationManager.getInstance().listerReservationParCLient(user.getIdUtilisateur());
		req.setAttribute("listeDeReservation",lstReservation);
		
		RequestDispatcher view = req.getRequestDispatcher("/reservationConnecte.jsp");
		view.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateurConnecte");
		
		String nomReservation = req.getParameter("Nom");
		String date = req.getParameter("bookDate");
		
		int nbPersonne = Integer.parseInt(req.getParameter("nbPersonne"));

		Integer idhoraire = Integer.parseInt(req.getParameter("horaire"));
		Horaire horaire = HoraireManager.getInstance().getUnHoraire(idhoraire);
	
		Integer idTable = Integer.parseInt(req.getParameter("table"));
		Table table = TableManager.getInstance().getUneTable(idTable);
	
	
		Reservation reservation =new Reservation(user,table, horaire,date,nomReservation,nbPersonne);
		ReservationManager.getInstance().ajouterReservation(reservation);
		
		String message = "Vous avez effectuez une reservation pour le "+reservation.getDate()+" à "+reservation.getHoraire().getIntervalle()+" au nom de "+reservation.getNomReservation()+".";
		SendTextMessage envoyeurDeMail = new SendTextMessage();
		try {
			envoyeurDeMail.envoyer_email("smtp.gmail.com", "465", "estaminet.howardries.resto@gmail.com",reservation.getUtilisateur().getMail(), "Confirmation de reservation",message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		req.getSession().setAttribute("reservation", reservation);
		
		resp.sendRedirect("ReservationReussi");
	}

	
}