package com.contraflow.cms.ESign.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/tenant/esign")
@RestController
public class ESignController {



    @GetMapping
    public String getEsignRequest(){
        return "esign request fetch successfully";
    }




    //Create Esign request for client
    @PostMapping
    public String CreateEsignRequest(){
        return "esign request create successfully";
    }

    //Create Esign request for client





    @PostMapping("/sign")
    public String CreateEsign(){
        return "sign successfully";
    }


}
