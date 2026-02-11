import React from "react";
import "./Testimonials.css";
import { assets } from "../../assets/assets";

const Testimonials = () => {
  return (
    <section className="testimonials-section" id="depoimentos">

      {/* TÍTULO */}
      <div className="testimonials-header">
        <h2>Depoimentos</h2>
        <p>Veja o que dizem nossos clientes:</p>
      </div>

      {/* CARDS */}
      <div className="testimonials-cards">

        {/* CARD 1 */}
        <div className="testimonial-card">
          <div className="card-top">
            <div className="user-info">
              <img src={assets.user1} alt="cliente" />
              <div>
                <h4>Saymon Junior</h4>
                <span>19/02/2023</span>
              </div>
            </div>

           
          </div>

          <div className="stars">★★★★★</div>

          <p className="testimonial-text">
            Pizza muito boa ótimo atendimento
          </p>
        </div>

        {/* CARD 2 */}
        <div className="testimonial-card">
          <div className="card-top">
            <div className="user-info">
              <img src={assets.user2} alt="cliente" />
              <div>
                <h4>Rafael Dornas</h4>
                <span>10/02/2023</span>
              </div>
            </div>

            
          </div>

          <div className="stars">★★★★★</div>

          <p className="testimonial-text">
            Top dms, atendente Leonel muito educado e gente boa.
            Indico
          </p>
        </div>

      </div>

      {/* GALERIA DE FOTOS */}
      <div className="testimonial-gallery">

        

        <div className="gallery-images">
          <img src={assets.galeria1} />
          <img src={assets.galeria2} />
          <img src={assets.galeria3} />
        </div>

        

        

      </div>
     
        {/* <a href="#explore-menu" className="btn-d">
              Ver Cardápio
            </a> */}
    </section>
    
  );
};

export default Testimonials;
