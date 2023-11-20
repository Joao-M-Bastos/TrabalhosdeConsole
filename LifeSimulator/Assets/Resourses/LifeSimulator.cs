using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LifeSimulator : MonoBehaviour
{
    public ComputeShader cShader;
    public GameObject quadPrefab;
    public int counts = 100;

    bool isPaused;

    Color lightGray = new Color(0.7f, 0.7f, 0.7f);

    struct Cells
    {
        public Vector3 position;
        public Color color;
    }


    Cells[] data;
    GameObject[] gameObjects;

    // Start is called before the first frame update
    void Start()
    {
        CreateCells();
        isPaused = true;
    }

    public void Update()
    {
        GetPlayerInputs();
        if (isPaused && !Input.GetKeyDown(KeyCode.LeftControl))
            return;

        //Verifica quais entre as celulas presisam ser atualizadas
        for (int i = 0; i < gameObjects.Length; i++)
        {
            
            QuadBehaviour quad = gameObjects[i].GetComponent<QuadBehaviour>();

            if (quad.IsAlive)
            {
                quad.TestIfNeedToDie();
                
                foreach (QuadBehaviour neighbors in quad.GetNeighbors())
                {
                    if(!neighbors.CanBorn)
                        neighbors.TestIfNeedToBorn();
                }
            }
        }

        //Atualiza as celulas que presisarem
        for (int i = 0; i < gameObjects.Length; i++)
        {
            QuadBehaviour quad = gameObjects[i].GetComponent<QuadBehaviour>();
            if (quad.CanBorn)
            {
                quad.Born();
                ChangeCellColor(i, true);
            }
            if(quad.CanDie)
            {
                quad.Die();
                ChangeCellColor(i, false);
            }
        }

        Debug.Log(Time.deltaTime);
    }


    private void ChangeCellColor(int id, bool willBorn)
    {
        int totalSize = sizeof(float) * 3 + sizeof(float) * 4;

        ComputeBuffer computeBuffer = new ComputeBuffer(data.Length, totalSize);
        computeBuffer.SetData(data);

        cShader.SetBuffer(0, "cells", computeBuffer);
        cShader.SetBool("willBorn", willBorn);
        cShader.Dispatch(0, data.Length / 8, 8, 1);

        computeBuffer.GetData(data);

        Debug.Log(data[id].color);

        gameObjects[id].GetComponent<MeshRenderer>().material.SetColor("_Color", data[id].color);
        

        computeBuffer.Dispose();
    }

    public QuadBehaviour[] returnNeighbors(int id)
    {
        int temp1 = 8;
        int temp2 = 0;
        bool top = true, bottom = true, left = true, right = true;

        if (id < counts)
        {
            top = false;
            temp1 -= 3;
        }else if(id >= gameObjects.Length - counts)
        {
            bottom = false;
            temp1 -= 3;
        }

        if(id % counts == 0)
        {
            left = false;
            temp1 -= 3;
        }else if (id % counts == counts - 1)
        {
            right = false;
            temp1 -= 3;
        }
        if (temp1 < 3)
            temp1 = 3;

        QuadBehaviour[] neighbors = new QuadBehaviour[temp1];

        if (top)
        {
            neighbors[temp2] = gameObjects[id - counts].GetComponent<QuadBehaviour>();
            temp2++;

            if (right)
            {
                neighbors[temp2] = gameObjects[id - counts + 1].GetComponent<QuadBehaviour>();
                temp2++;
            }
            
            if (left)
            {
                neighbors[temp2] = gameObjects[id - counts - 1].GetComponent<QuadBehaviour>();
                temp2++;
            }

        }
        if (bottom)
        {
            neighbors[temp2] = gameObjects[id + counts].GetComponent<QuadBehaviour>();
            temp2++;

            if (right)
            {
                neighbors[temp2] = gameObjects[id + counts + 1].GetComponent<QuadBehaviour>();
                temp2++;
            }

            if (left)
            {
                neighbors[temp2] = gameObjects[id + counts - 1].GetComponent<QuadBehaviour>();
                temp2++;
            }
        }

        if (right)
        {
            neighbors[temp2] = gameObjects[id + 1].GetComponent<QuadBehaviour>();
            temp2++;
        }

        if (left)
        {
            neighbors[temp2] = gameObjects[id - 1].GetComponent<QuadBehaviour>();
        }

        return neighbors;
    }

    private void GetPlayerInputs()
    {
        Vector3 mausePos = Input.mousePosition;
        mausePos.z = 10;
        mausePos = Camera.main.ScreenToWorldPoint(mausePos);

        if (Input.GetMouseButtonDown(0))
        {
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit hit;

            if(Physics.Raycast(ray, out hit, 15))
            {
                QuadBehaviour cell = hit.transform.gameObject.GetComponent<QuadBehaviour>();

                if (cell.IsAlive)
                    cell.Die();
                else
                    cell.Born();

                ChangeCellColor(cell.Id, cell.IsAlive);
            }
        }
        
        if (Input.GetKeyDown(KeyCode.Space))
            isPaused = !isPaused;
        if (Input.GetKeyDown(KeyCode.Delete))
            RecreateCells();
        if (Input.GetKeyDown(KeyCode.Escape))
            Application.Quit();
    }

    private void RecreateCells()
    {
        isPaused = true;

        for(int i = 0; i < data.Length; i++)
        {
            Destroy(gameObjects[i]);
        }

        CreateCells();
    }

    public void CreateCells()
    {
        data = new Cells[counts * counts];
        gameObjects = new GameObject[counts * counts];

        for (int i = 0; i < counts; i++)
        {
            float offsetX = (-counts / 2 + i);

            for (int j = 0; j < counts; j++)
            {
                float offsetY = (-counts / 2 + j);

                GameObject go = Instantiate(quadPrefab, new Vector3(offsetX * 0.6f, 0, offsetY * 0.6f), Quaternion.identity);
                go.GetComponent<MeshRenderer>().material.SetColor("_Color", lightGray);
                go.transform.Rotate(90, 0, 0);

                int currentId = i * counts + j;

                gameObjects[currentId] = go;

                data[currentId] = new Cells();
                data[currentId].position = go.transform.position;
                data[currentId].color = lightGray;


            }
        }
        for (int i = 0; i < gameObjects.Length; i++)
        {
            gameObjects[i].GetComponent<QuadBehaviour>().SetNeighbors(returnNeighbors(i));
            gameObjects[i].GetComponent<QuadBehaviour>().SetId(i);
        }
    }
}
