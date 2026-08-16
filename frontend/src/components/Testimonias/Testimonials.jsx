import React from "react";
import "./Testimonials.css";
import { assets } from "../../assets/assets";
import TestimonialCard from "./TestimonialCard/TestimonialCard";

const Testimonials = () => {
  // Simulação dos dados que virão do seu backend futuramente
  const testimonialsData = [
    {
      id: 1,
      userName: "Saymon Junior",
      userImg: assets.user1,
      date: "19/02/2026",
      stars: 5,
      text: "Meu negócio duplicou os lucros depois que comecei a usar os serviços da Pendezzas Pizza. Atendimento excelente e qualidade incomparável!"
    },
    {
      id: 2,
      userName: "Rafael Dornas",
      userImg: assets.user2,
      date: "10/02/2026",
      stars: 5,
      text: "Top dms, o João Rafael é um cara super gente boa, sempre disposto a ajudar e tirar dúvidas. Recomendo demais! Eu pediria só pelos entregadores que é um gatinho."
    }
  ];

  const galleryImages = [assets.galeria1, assets.galeria2, assets.galeria3];

  return (
    <section className="testimonials-section" id="depoimentos">
      <div className="testimonials-header">
        <h2>Depoimentos</h2>
        <p>Veja o que dizem nossos clientes:</p>
      </div>

      <div className="testimonials-cards">
        {testimonialsData.map((item) => (
          <TestimonialCard 
            key={item.id}
            userName={item.userName}
            userImg={item.userImg}
            date={item.date}
            stars={item.stars}
            text={item.text}
          />
        ))}
      </div>

      <div className="testimonial-gallery">
        <div className="gallery-images">
          {galleryImages.map((img, index) => (
            <img key={index} src={img} alt={`Galeria ${index}`} loading="lazy" />
          ))}
        </div>
      </div>
    </section>
  );
};

export default Testimonials;