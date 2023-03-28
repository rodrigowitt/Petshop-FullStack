
import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Cliente, Petshop } from 'src/app/petshop';
import { PetshopService } from 'src/app/petshop.service';





@Component({
  selector: 'app-pets',
  templateUrl: './pets.component.html',
  styleUrls: ['./pets.component.css']
})
export class PetsComponent {
  public pet : Petshop [] = [];
  public cliente: Cliente[] = [];
  public editPet : Petshop | null;
  public deletePet : Petshop | null;
  
  constructor ( private petshopService: PetshopService){}
  
  ngOnInit(): void {
    this.getTratamentos();
    
  }

  public getTratamentos():void{
    this.petshopService.getPet().subscribe(
      (response: Petshop[])=>{
        this.pet = response;
      }, (error: HttpErrorResponse) => {alert(error.message)}
    
      )
     }

     

     public onAddPet(addForm: NgForm) : void{
      document.getElementById('add-pet-form')?.click();
        this.petshopService.addPet(addForm.value).subscribe(
          (response: Petshop) => {
            console.log(response)
            addForm.reset();
            this.getTratamentos();
    
          },
          (error: HttpErrorResponse)=>{
            alert(error.message)
          }
          )
         
        }
        public onAddCliente(addForm: NgForm) : void{
          document.getElementById('add-cliente-form')?.click();
            this.petshopService.addClient(addForm.value).subscribe(
              (response: Cliente) => {
                console.log(response)
                this.getTratamentos();
                addForm.reset();
        
              },
              (error: HttpErrorResponse)=>{
                alert(error.message)
              }
              )
            }

        public onUpdatePet(petshop: Petshop, petId : string | undefined) :void{
        
          this.petshopService.updatePet(petId, petshop).subscribe(
            (response: Petshop)=>{
              console.log(response);
              this.getTratamentos();
            },
            (error: HttpErrorResponse)=>{
              alert(error.message)
            }
          )
          }

          public onDeletePet(petId: string | undefined) :void{
            
            this.petshopService.deletePet(petId).subscribe(
              (response : void) =>{
                this.getTratamentos();
                },
                (error: HttpErrorResponse)=>{
                  alert(error.message)
                }
            )
          }

          

          public onOpenModal(petshop: Petshop | null, mode: String): void{
            const container = document.getElementById('main-container')
            const button = document.createElement('button');
            button.type = 'button';
            button.style.display = 'none';
            button.setAttribute('data-toggle', 'modal')
            if(mode === 'add'){
              button.setAttribute('data-target', '#addPetModal')
            }
            if(mode === 'addClient'){
              button.setAttribute('data-target', '#addClienteModal')
            }
            if(mode === 'update'){
              this.editPet = petshop;
              button.setAttribute('data-target', '#updatePetModal2')
            }
            if(mode === 'delete'){
              this.deletePet = petshop;
              button.setAttribute('data-target', '#deletePetModal2')
            }
            container?.appendChild(button);
            button.click();
          }

          public searchTratamentos(key: string) : void{
            const results: Petshop[] = [];
            for(const tratamento of this.pet){
              if(tratamento.nome?.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
              tratamento.raca?.toLowerCase().indexOf(key.toLowerCase()) !== -1
              
               ){
                
                results.push(tratamento)
              }
            }
            this.pet = results;
            if(results.length === 0 || !key){
              this.getTratamentos();
            }
          }



    

}
