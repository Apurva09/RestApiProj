package com.examples.TradeCoin.Repository;

import com.examples.TradeCoin.Entity.Coins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptRepo  extends JpaRepository<Coins,Long> {
    List<Coins> findByName(String name);
}
