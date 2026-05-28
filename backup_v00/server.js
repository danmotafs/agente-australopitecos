const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// Simulação: Buscar dados do usuário
app.get('/api/usuarios/:id', (req, res) => {
  const { id } = req.params;
  console.log(`Buscando dados do usuário ID: ${id}`);

  // Simulação de resposta
  res.json({
    id,
    nome: "Usuário Exemplo",
    assinaturaAtiva: true,
    telefone: "+5511999999999"
  });
});

// Simulação: Ativar modo furtivo
app.post('/api/ativar-furtivo/:id', (req, res) => {
  const { id } = req.params;
  console.log(`Ativando modo furtivo para o usuário ID: ${id}`);
  res.json({ status: "modo furtivo ativado", id });
});

app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});
