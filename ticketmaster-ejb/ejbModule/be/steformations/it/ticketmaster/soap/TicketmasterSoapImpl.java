package be.steformations.it.ticketmaster.soap;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import be.steformations.it.ticketmaster.beans.Client;
import be.steformations.it.ticketmaster.beans.Evenement;
import be.steformations.it.ticketmaster.beans.Reservation;
import be.steformations.it.ticketmaster.beans.Salle;
import be.steformations.it.ticketmaster.dto.ClientDto;
import be.steformations.it.ticketmaster.dto.EvenementDto;
import be.steformations.it.ticketmaster.dto.ReservationDto;
import be.steformations.it.ticketmaster.dto.SalleDto;
import be.steformations.it.ticketmaster.jms.server.TicketmasterJmsMessageProducer;
import be.steformations.it.ticketmaster.jpa.JpaTicketmaster;

@javax.ejb.Stateless
@javax.jws.WebService(targetNamespace = TicketmasterSoap.targetNamespace, serviceName = TicketmasterSoap.serviceName, endpointInterface = TicketmasterSoap.endpointInterface)
public class TicketmasterSoapImpl implements TicketmasterSoap {

	@javax.persistence.PersistenceContext(unitName = "ticketmaster")
	javax.persistence.EntityManager em;
	@javax.ejb.EJB
	private TicketmasterJmsMessageProducer messageProducer;

	private JpaTicketmaster jpa;

	public TicketmasterSoapImpl() {
		super();
		System.out.println("TicketmasterSoapImpl.TicketmasterSoapImpl()");
	}

	@javax.annotation.PostConstruct // méthode invoquée directement après
									// l'instanciation
	private void init() {
		System.out.println("TicketmasterSoapImpl.init()");
		this.jpa = new JpaTicketmaster(this.em);
	}

	@Override
	public ClientDto getClientById(int id) {
		System.out.println("TicketmasterSoapImpl.getClientById()");
		return this.createClient(this.jpa.getClientById(id));
	}

	@Override
	public EvenementDto getEvenementById(int id) {
		System.out.println("TicketmasterSoapImpl.getEvenementById()");
		return this.createEvent(this.jpa.getEvenementById(id));
	}

	@Override
	public EvenementDto createEvenement(String nom, Date jour, Date heure, Salle salle) {
		System.out.println("TicketmasterSoapImpl.createEvenement()");

		EvenementDto dto = this.createEvent(this.jpa.createEvenement(nom, jour, heure, salle));
		System.out.println("TicketmasterSoapImpl.createEvenement() broadcastMachin");
		this.messageProducer.broadcastNewTicketmasterEvent(dto);
		return dto;
	}

	@Override
	public EvenementDto createEvenementString(String nom, String jour, String heure, int salleId) {
		System.out.println("TicketmasterSoapImpl.createEvenementString()");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		Date hour = new Date();
		System.out.println("date: " + jour);
		System.out.println("heure: " + heure);
		EvenementDto event = new EvenementDto();
		Salle salle = this.jpa.getSalleById(salleId);
		try {
			date = dateFormatter.parse(jour);
			hour = hourFormatter.parse(heure);
			System.out.println("date formatée: " + date);
			System.out.println("heure formatée: " + hour);
			event = createEvenement(nom, date, hour, salle);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return event;
	}

	@Override
	public EvenementDto removeEvenement(int id) {
		System.out.println("TicketmasterSoapImpl.removeEvenement()");
		return this.createEvent(this.jpa.removeEvenement(id));

	}

	@Override
	public ReservationDto getReservationById(int id) {
		System.out.println("TicketmasterSoapImpl.getReservationById()");
		return this.createReservation(this.jpa.getReservationById(id));
	}

	@Override
	public ReservationDto createReservationInt(int eventId, int clientId) {
		System.out.println("TicketmasterSoapImpl.createReservation()");
//		return this.createReservation(this.jpa.createReservation(eventId, clientId));
		return createReservation(this.jpa.getEvenementById(eventId), this.jpa.getClientById(clientId));
	}
	
	@Override
	public ReservationDto createReservation(Evenement evenement, Client client) {
		System.out.println("TicketmasterSoapImpl.createReservation()");
		return this.createReservation(this.jpa.createReservation(evenement, client));
	}

	@Override
	public SalleDto getSalleById(int id) {
		System.out.println("TicketmasterSoapImpl.getSalleById()");
		return this.createSalle(this.jpa.getSalleById(id));
	}

	private ClientDto createClient(Client client) {
		ClientDto dto = null;
		if (client != null) {
			dto = new ClientDto();
			dto.setId(client.getId());
			dto.setNom(client.getNom());
			dto.setIban(client.getIban());
			dto.setEmail(client.getEmail());
		}
		return dto;
	}

	private EvenementDto createEvent(Evenement event) {
		EvenementDto dto = null;
		if (event != null) {
			dto = new EvenementDto();
			dto.setId(event.getId());
			dto.setNom(event.getNom());
			dto.setJour(event.getJour());
			dto.setHeure(event.getHeure());
			dto.setSalle(this.createSalle(event.getSalle()));
		}
		return dto;
	}

	private ReservationDto createReservation(Reservation reservation) {
		ReservationDto dto = null;
		if (reservation != null) {
			dto = new ReservationDto();
			dto.setId(reservation.getId());
			dto.setEvenement(this.createEvent(reservation.getEvenement()));
			dto.setClient(this.createClient(reservation.getClient()));
		}
		return dto;
	}

	private SalleDto createSalle(Salle salle) {
		SalleDto dto = null;
		if (salle != null) {
			dto = new SalleDto();
			dto.setId(salle.getId());
			dto.setNom(salle.getNom());
			dto.setCapacite(salle.getCapacite());
		}
		return dto;
	}

}
