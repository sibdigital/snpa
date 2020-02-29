package ru.p03.snpa.services;


import retrofit2.Call;
import retrofit2.http.GET;
import ru.p03.snpa.entity.from1c.*;

public interface ServiceAPI {

    @GET("webaction/webaction")
    Call<Action> getAllActions();

    @GET("webls/webls")
    Call<LifeSituation> getAllLifeSituation();

    @GET("webpt/webpt")
    Call<PaymentType> getAllPaymentType();

    @GET("webpra/webpra")
    Call<Practice> getAllPractice();

    @GET("webpraatr/webpraatr")
    Call<PracticeAttribute> getAllPracticeAttribute();

    @GET("webprapra/webprapra")
    Call<PracticePractice> getAllPracticePractice();

    @GET("web_attribute_type/web_attribute_type")
    Call<AttributeType> getAllAttributeType();

    @GET("web_attribute_value/web_attribute_value")
    Call<AttributeValue> getAllAttributeValue();

    @GET("webq/webq")
    Call<Question> getAllQuestions();
}
