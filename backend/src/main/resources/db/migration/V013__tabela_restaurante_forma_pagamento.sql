CREATE TABLE restaurante_forma_pagamento (
    restaurante_id BINARY(16) NOT NULL,
    forma_pagamento_id BIGINT NOT NULL,
    PRIMARY KEY (restaurante_id, forma_pagamento_id),
    CONSTRAINT fk_restaurante FOREIGN KEY (restaurante_id)
        REFERENCES restaurante(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_forma_pagamento FOREIGN KEY (forma_pagamento_id)
        REFERENCES forma_pagamento(id)
        ON DELETE CASCADE
);
