import React from 'react';
import { fmt, fmtDate } from '../utils/formatter';

const DailyTable = ({ breakdown }) => (
  <div className="sr-table-wrapper">
    <table className="sr-table">
      <thead>
        <tr>
          {['Data', 'Pedidos', 'Faturado', 'Ticket médio'].map((h) => (
            <th key={h} className="sr-table__th">{h}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {breakdown
          .slice()
          .sort((a, b) => a.date.localeCompare(b.date))
          .map((d) => (
            <tr key={d.date} className="sr-table__row">
              <td className="sr-table__td">{fmtDate(d.date)}</td>
              <td className="sr-table__td">{d.totalSales}</td>
              <td className="sr-table__td">{fmt(d.totalBilled)}</td>
              <td className="sr-table__td">{fmt(d.averageTicket)}</td>
            </tr>
          ))}
      </tbody>
    </table>
  </div>
);

export default DailyTable;