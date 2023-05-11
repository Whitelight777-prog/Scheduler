package com.example.androidstudioproject_tomroche.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject_tomroche.entities.Term;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(entity = Term.class)
    void insertAll(Term...terms);

    @Delete(entity = Term.class)
    void delete(Term term);

    @Query("select * from terms")
    List<Term> getAll();

    @Update(entity = Term.class)
    void update(Term...term);

    @Query("select * from terms where :id=termId")
    Term getTermById(long id);

}
