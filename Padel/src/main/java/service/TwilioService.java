package service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {
    // Informations d'authentification Twilio
    private static final String ACCOUNT_SID = "AC853b6977b68c39a74b5e380b34d0e31c";
    private static final String AUTH_TOKEN = "5406e7f330df7ae3fadb295e63d3870a";
    private static final String TWILIO_PHONE_NUMBER = "+19403146640";

    // Initialiser Twilio
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    /**
     * Envoyer un SMS à un numéro spécifié.
     *
     * @param toPhoneNumber Numéro de téléphone du destinataire (format international, ex. +21655596014)
     * @param messageBody   Contenu du message
     */
    public static void sendSMS(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber), // Numéro du destinataire
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Numéro Twilio
                    messageBody // Contenu du message
            ).create();

            System.out.println("Message envoyé avec succès ! SID : " + message.getSid());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}