package com.IT342.DeliverYey.Service;

import com.IT342.DeliverYey.Entity.DeliveryEntity;
import com.IT342.DeliverYey.Entity.StudentEntity;
import com.IT342.DeliverYey.Repository.DeliveryRepository;
import com.IT342.DeliverYey.Repository.StudentRepository;
import com.IT342.DeliverYey.UserType;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliveryService {

    // Logger for the service class
    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private StudentRepository studentRepository;

    public DeliveryEntity insertDelivery(DeliveryEntity delivery) {
        try {
            // Save the delivery
            deliveryRepository.save(delivery);

            // Assuming you want to update the student's user type if it's not null
            StudentEntity student = delivery.getStudent();
            if (student != null) {
                student.setUserType(UserType.DELIVERY);
                studentRepository.save(student);
            }

            return delivery;
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return null or handle the error appropriately
            return null;
        }
    }

    public List<DeliveryEntity> getAllDelivery() {
        List<DeliveryEntity> deliveryList = deliveryRepository.findAll();

        // Initialize the student association for each delivery
        deliveryList.forEach((delivery) -> {
            Hibernate.initialize(delivery.getStudent());
        });

        return deliveryList;
    }

    public DeliveryEntity updateDelivery(Long deliveryId, DeliveryEntity newDeliveryDetails) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoSuchElementException("Delivery with ID " + deliveryId + " not found."));

        delivery.setPhoneNumber(newDeliveryDetails.getPhoneNumber());
        delivery.setLocation(newDeliveryDetails.getLocation());
        delivery.setPaymentMethod(newDeliveryDetails.getPaymentMethod());
        delivery.setNotes(newDeliveryDetails.getNotes());

        return deliveryRepository.save(delivery);
    }

    public String deleteDelivery(Long deliveryId) {
        if (deliveryRepository.existsById(deliveryId)) {
            deliveryRepository.deleteById(deliveryId);
            return "Delivery with ID " + deliveryId + " has been deleted.";
        } else {
            throw new NoSuchElementException("Delivery with ID " + deliveryId + " not found.");
        }
    }
}
