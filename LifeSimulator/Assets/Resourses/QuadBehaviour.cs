using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class QuadBehaviour : MonoBehaviour
{
    [SerializeField] bool isAlive;

    public bool IsAlive => isAlive;

    [SerializeField] bool canBorn;
    public bool CanBorn => canBorn;

    [SerializeField] bool canDie;
    public bool CanDie => canDie;

    QuadBehaviour[] neighbors;

    int id;

    public int Id => id;
    // Start is called before the first frame update
    public void SetNeighbors(QuadBehaviour[] _neighbors)
    {
        neighbors = _neighbors;
    }

    public void SetId(int i)
    {
        id = i;
    }

    public void Die()
    {
        isAlive = false;
        canDie = false;
    }

    public void Born(){
        isAlive = true;
        canBorn = false;
    }

    public QuadBehaviour[] GetNeighbors()
    {
        return neighbors;
    }

    public void TestIfNeedToDie()
    {
        
        if(NumOfAliveNeighbors() > 3 || NumOfAliveNeighbors() < 2)
            canDie = true;
    }

    public void TestIfNeedToBorn()
    {
        if (NumOfAliveNeighbors() == 3)
            canBorn = true;
    }

    private int NumOfAliveNeighbors()
    {
        int numOfNeighbors = 0;

        foreach(QuadBehaviour neighbor in neighbors)
        {
            if(neighbor.IsAlive)
                numOfNeighbors++;
            
        }

        return numOfNeighbors;
    }
}
