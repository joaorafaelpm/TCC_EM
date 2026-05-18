import React from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip,
  ResponsiveContainer,
} from 'recharts';
import CustomTooltip from './CustomTooltip';

const ProductsChart = ({ data }) => (
  <ResponsiveContainer width="100%" height={Math.max(160, data.length * 44 + 60)}>
    <BarChart layout="vertical" data={data} margin={{ top: 4, right: 24, left: 8, bottom: 0 }}>
      <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" horizontal={false} />
      <XAxis type="number" tick={{ fontSize: 12 }} allowDecimals={false} />
      <YAxis type="category" dataKey="name" tick={{ fontSize: 12 }} width={160} />
      <Tooltip content={<CustomTooltip />} />
      <Bar dataKey="Quantidade" fill="#2c3e50" radius={[0, 4, 4, 0]} />
    </BarChart>
  </ResponsiveContainer>
);

export default ProductsChart;