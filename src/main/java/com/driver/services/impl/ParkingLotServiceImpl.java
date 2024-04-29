package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot= new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        List<Spot> spots = new ArrayList<>();
        parkingLot.setSpotList(spots);
         return parkingLotRepository1.save(parkingLot);

    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
          Spot spot= new Spot();
          spot.setPricePerHour(pricePerHour);
           // number of wheels
          if(numberOfWheels<=2){
              spot.setSpotType(SpotType.TWO_WHEELER);
          }
          else if(numberOfWheels<=4){
              spot.setSpotType(SpotType.FOUR_WHEELER);
          }
          else
              spot.setSpotType(SpotType.OTHERS);

          ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();
          spot.setParkingLot(parkingLot);
          spot.setOccupied(false);
          spot.setReservationList(new ArrayList<>());
          parkingLot.getSpotList().add(spot);
          parkingLotRepository1.save(parkingLot);
          return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Optional<Spot> spotOptional = spotRepository1.findById(spotId);
        if(spotOptional.isPresent()) {
            Spot spot= spotOptional.get();
            ParkingLot parkingLot = spot.getParkingLot();
            List<Spot> spotList = parkingLot.getSpotList();
            for(Spot s:spotList){
                if(s.getId()==spotId){
                    spotList.remove(s);
                    break;
                }
            }
            spotRepository1.deleteById(spotId);
            parkingLot.setSpotList(spotList);
            parkingLotRepository1.save(parkingLot);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

       Optional<ParkingLot> parkingLotOptional= parkingLotRepository1.findById(parkingLotId);
       boolean isSpotIdPresent= false;
       if(parkingLotOptional.isPresent()){
           List<Spot> spotList = parkingLotOptional.get().getSpotList();
           for(Spot s : spotList){
               if(s.getId()==spotId){
                   isSpotIdPresent=true;
                   s.setPricePerHour(pricePerHour);
                   break;
               }
           }
           if(isSpotIdPresent) {
               parkingLotRepository1.save(parkingLotOptional.get());
               return spotRepository1.findById(spotId).get();
           }
       }
       return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        if(parkingLotOptional.isPresent()){
            parkingLotRepository1.deleteById(parkingLotId);
        }
    }
}
