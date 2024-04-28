package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
         User user = null;
        Optional<User> optionalUser= userRepository3.findById(userId);
        if(optionalUser.isPresent()){
            user= optionalUser.get();
        }
        else{
            throw new Exception("the user is not found");
        }

        ParkingLot parkingLot = null;
        Optional<ParkingLot> optionalParkingLot= parkingLotRepository3.findById(parkingLotId);
        if(optionalParkingLot.isPresent()){
            parkingLot= optionalParkingLot.get();
        }
        else{
            throw new Exception("the parking lot is not found");
        }

        List<Spot> spotList = parkingLot.getSpotList();
        Spot spotFound= null;
        int price =Integer.MAX_VALUE;

        for(Spot s:spotList){

            int wheels=0;
            if(s.getSpotType()==SpotType.TWO_WHEELER){
                wheels=2;
            }
            else if(s.getSpotType()==SpotType.FOUR_WHEELER){
                wheels=4;
            }
            else
                wheels=24;

            if(s.getOccupied().equals(false) && numberOfWheels<=wheels){
                 price= Math.min(price,s.getPricePerHour());
                spotFound=s;
            }
        }
        if(spotFound==null)
            throw new Exception("No spot is available for reservation");

        Reservation reservation = new Reservation();
        reservation.setSpot(spotFound);
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        spotFound.setOccupied(true);
        spotRepository3.save(spotFound);
        Reservation savedReservation =  reservationRepository3.save(reservation);
        spotFound.getReservationList().add(savedReservation);
        return savedReservation;

    }
}
