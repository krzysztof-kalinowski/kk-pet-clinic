package kkalinowski.springframework.kkpetclinic.bootstrap;

import kkalinowski.springframework.kkpetclinic.model.*;
import kkalinowski.springframework.kkpetclinic.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by Krzysztof Kalinowski on 15/11/2019.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final OwnerService ownerService;
    private final VetService vetService;
    private final PetTypeService petTypeService;
    private final SpecialityService specialityService;
    private final VisitService visitService;

    public DataLoader(OwnerService ownerService, VetService vetService, PetTypeService petTypeService, SpecialityService specialityService, VisitService visitService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.petTypeService = petTypeService;
        this.specialityService = specialityService;
        this.visitService = visitService;
    }

    @Override
    public void run(String... args) throws Exception {

        if(petTypeService.findAll().size() == 0){
            loadData();
        }
    }

    private void loadData() {
        PetType petType1 = new PetType();
        petType1.setName("Dog");
        PetType savedDogPetType = petTypeService.save(petType1);

        PetType petType2 = new PetType();
        petType2.setName("Cat");
        PetType savedCatPetType = petTypeService.save(petType2);

        System.out.println("Loaded PetTypes...");

        Owner owner1 = new Owner();
        owner1.setFirstName("Michał");
        owner1.setLastName("Sapacz");
        owner1.setAddress("Szatańska 66");
        owner1.setCity("Warszawa");
        owner1.setTelephone("666666666");

        Pet michalPet = new Pet();
        michalPet.setPetType(savedDogPetType);
        michalPet.setOwner(owner1);
        michalPet.setBirthDate(LocalDate.now());
        michalPet.setName("Micha");

        owner1.getPets().add(michalPet);

        ownerService.save(owner1);

        Visit michaVisit = new Visit();
        michaVisit.setPet(michalPet);
        michaVisit.setDate(LocalDate.now());
        michaVisit.setDescription("pierwsza wizyta kontrolna");
        visitService.save(michaVisit);

        Owner owner2 = new Owner();
        owner2.setFirstName("Patrycja");
        owner2.setLastName("Budzik");
        owner2.setAddress("Zegarowa 5");
        owner2.setCity("Jaroszówka");
        owner2.setTelephone("123456789");

        Pet patrycjaPet = new Pet();
        patrycjaPet.setPetType(savedCatPetType);
        patrycjaPet.setOwner(owner2);
        patrycjaPet.setBirthDate(LocalDate.now());
        patrycjaPet.setName("Katka");
        owner2.getPets().add(patrycjaPet);

        ownerService.save(owner2);

        System.out.println("Loaded Owners....");


        Speciality radiology = new Speciality();
        radiology.setDescription("Radiology");
        Speciality savedRadiology = specialityService.save(radiology);

        Speciality surgery = new Speciality();
        surgery.setDescription("Surgery");
        Speciality savedSurgery = specialityService.save(surgery);

        Speciality dentistry = new Speciality();
        dentistry.setDescription("Dentistry");
        Speciality savedDentistry = specialityService.save(dentistry);

        System.out.println("Loaded Specialities....");

        Vet vet1 = new Vet();
        vet1.setFirstName("Krzysztof");
        vet1.setLastName("Kalinowski");
        vet1.getSpecialities().add(savedRadiology);
        vetService.save(vet1);

        Vet vet2 = new Vet();
        vet2.setFirstName("Zdzisław");
        vet2.setLastName("Mietkowski");
        vet2.getSpecialities().add(savedSurgery);
        vet2.getSpecialities().add(savedDentistry);
        vetService.save(vet2);

        System.out.println("Loaded Vets...");
    }
}
