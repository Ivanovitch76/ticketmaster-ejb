package be.steformations.it.ticketmaster.soap;

import be.steformations.it.ticketmaster.beans.Client;
import be.steformations.it.ticketmaster.beans.Evenement;
import be.steformations.it.ticketmaster.beans.Salle;
import be.steformations.it.ticketmaster.dao.TicketmasterDao;
import be.steformations.it.ticketmaster.dto.ClientDto;
import be.steformations.it.ticketmaster.dto.EvenementDto;
import be.steformations.it.ticketmaster.dto.ReservationDto;
import be.steformations.it.ticketmaster.dto.SalleDto;

@javax.jws.WebService
public interface TicketmasterSoap {

	String wsdl = "/ws/TicketmasterSoap?wsdl";
	String targetNamespace = "be.steformations.it.ticketmaster.soap";
	String serviceName = "TicketmasterSoap";
	String endpointInterface = "be.steformations.it.ticketmaster.soap.TicketmasterSoap";
	
	ClientDto getClientById(int id);
	EvenementDto getEvenementById(int id);
	EvenementDto createEvenement(String nom, java.util.Date jour, java.util.Date heure, Salle salle);
	EvenementDto createEvenementString(String nom, String jour, String heure, int salleId);
	EvenementDto removeEvenement(int id);
	ReservationDto getReservationById(int id);
	ReservationDto createReservationInt(int eventId, int clientId);
	ReservationDto createReservation(Evenement evenement, Client client);
	SalleDto getSalleById(int id);
}
