package com.examples.TradeCoin.Controller;

import com.examples.TradeCoin.Entity.Coins;
import com.examples.TradeCoin.Repository.CryptRepo;
import com.examples.TradeCoin.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class CryptControl {
    @Autowired
    private CryptRepo coinrepo;
    //get  all listed coins
    @GetMapping("coins")
    public List<Coins> getAllCoins()
    {
        return this.coinrepo.findAll();
    }
    //get coin by id
    @GetMapping("/coins/{id}")
    public ResponseEntity<Coins> getCoinsById(@PathVariable(value="id") long coinsId)
            throws ResourceNotFoundException
    {
        Coins coins=coinrepo.findById(coinsId)
                .orElseThrow(() -> new ResourceNotFoundException("coins not found " +coinsId));
        return  ResponseEntity.ok().body(coins);
    }
    /*  //get listed coin by name
      @GetMapping("/coinss/name")
      public ResponseEntity<List<Coins>> getCoinsByName(@RequestParam String name)
              throws ResourceNotFoundException
      {
         return new ResponseEntity<List<Coins>>(coinrepo.findByName(name), HttpStatus.OK);
      }*/
    //List coin
    @PostMapping("coinss")
    public Coins addCoins(@RequestBody Coins coins)
    {    System.out.println("inside add");
        return  this.coinrepo.save(coins);

    }
    //update coin price
    @PutMapping("coins/{id}")
    public ResponseEntity<Coins> updateCoins(@PathVariable(value="id") Long coinsId, @RequestBody Coins coinDetails) throws ResourceNotFoundException {
        Coins coins=coinrepo.findById(coinsId)
                .orElseThrow(()-> new ResourceNotFoundException("coins not found for this" +coinDetails.getName() +coinsId));
        //coins.setName(coinDetails.getName());
        coins.setPrice(coinDetails.getPrice());
        //coins.setBalance(coinDetails.getBalance());
        return ResponseEntity.ok(this.coinrepo.save(coins));
    }
   /* @PutMapping("Coins/{id}")
    public ResponseEntity<Coins> unlistCoin(@PathVariable (value="id")Long CoinId,@RequestBody Coins newCoin) {

        return coinrepo.findById(CoinId)
                .map(coins -> {
                    coins.setName(newCoin.getName());
                    coins.setPrice(newCoin.getPrice());
                    return ResponseEntity.ok(this.coinrepo.save(coins));
                })
                .orElseGet(() -> {
                    newCoin.setId(CoinId);
                    return ResponseEntity.ok(this.coinrepo.save(newCoin));
                });
    }*/

    //Unlist the coin
    @DeleteMapping("coins/{id}")
    public Map<String, Boolean> deleteCoins(@PathVariable(value="id") Long coinsId) throws ResourceNotFoundException {
        Coins coins=coinrepo.findById(coinsId)
                .orElseThrow(()->new ResourceNotFoundException("coins not found to delete" +coinsId));
        this.coinrepo.delete(coins);
        Map<String,Boolean> response=new HashMap<>();
        response.put("Listed Coins removed",Boolean.TRUE);
        return response;
    }
    //Buy or Sell Coin
    @PutMapping("/addAmount/{id}")
    public ResponseEntity<Coins> addAmount(@PathVariable(value="id") Long coinsId, @RequestBody Coins coinObj) throws ResourceNotFoundException {
        Coins coins=coinrepo.findById(coinsId)
                .orElseThrow(()-> new ResourceNotFoundException(" Coin name not found"  +coinObj.getName()));
        try{
            if (coinObj.getCategory().equalsIgnoreCase("buy") && (coinObj.getPrice()<=coins.getBalance()) && coins.getName().equals(coinObj.getName())) {
                coins.setBalance(coins.getBalance() - coinObj.getPrice());
                coins.setPrice((coins.getPrice() + coinObj.getPrice()));
                coins.setCategory(coinObj.getCategory());
            }

            if (coinObj.getCategory().equalsIgnoreCase("sell") && coins.getName().equals(coinObj.getName())){

                coins.setBalance(coins.getBalance() +coinObj.getPrice());
                coins.setPrice((coins.getPrice() -coinObj.getPrice()));
                coins.setCategory(coinObj.getCategory());
            }
            else{
                System.out.println("Not enough balance or Coin name not found");
                // return ResponseEntity.of(Optional.of(coinObj));
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message","Not enough balance or Coin name not found" );
                errorResponse.put("status", HttpStatus.NOT_FOUND.toString());
                return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
            }
            if (coinObj.getCategory().equalsIgnoreCase("exchange") && (coinObj.getPrice()<=coins.getBalance() && coins.getPrice()>=coinObj.getPrice())) {
              coins.setName(coinObj.getName());
              coins.setPrice(coins.getPrice()-coinObj.getPrice());
            }
            }catch(IndexOutOfBoundsException e){
            //handle exception
        }
        return ResponseEntity.ok(this.coinrepo.save(coins));
    }
}
