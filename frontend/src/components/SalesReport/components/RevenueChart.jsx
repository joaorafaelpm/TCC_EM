import React from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip,
  ResponsiveContainer, Legend,
} from 'recharts';
import CustomTooltip from './CustomTooltip';

const RevenueChart = ({ data }) => (
  <ResponsiveContainer width="100%" height={220}>
    <BarChart data={data} margin={{ top: 4, right: 16, left: 0, bottom: 0 }}>
      <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
      <XAxis dataKey="date" tick={{ fontSize: 12 }} />
      <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `R$${v}`} />
      <Tooltip content={<CustomTooltip />} />
      <Legend wrapperStyle={{ fontSize: 12 }} />
      <Bar dataKey="Faturado"     fill="#c0392b" radius={[4, 4, 0, 0]} />
      <Bar dataKey="Ticket médio" fill="#e67e22" radius={[4, 4, 0, 0]} />
    </BarChart>
  </ResponsiveContainer>
);

export default RevenueChart;