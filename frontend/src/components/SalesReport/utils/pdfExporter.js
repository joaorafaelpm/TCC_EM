const loadScript = (src) =>
  new Promise((resolve, reject) => {
    const s = document.createElement("script");
    s.src = src;
    s.onload = resolve;
    s.onerror = reject;
    document.head.appendChild(s);
  });

export const exportReportPDF = async ({
  ref,
  restaurantName,
  startDate,
  endDate,
}) => {
  if (!globalThis.html2canvas)
    await loadScript(
      "https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js",
    );
  if (!globalThis.jspdf)
    await loadScript(
      "https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js",
    );

  const canvas = await globalThis.html2canvas(ref.current, {
    scale: 2,
    useCORS: true,
    backgroundColor: "#ffffff",
  });

  const { jsPDF } = globalThis.jspdf;
  const pdf = new jsPDF({ orientation: "portrait", unit: "px", format: "a4" });
  const pdfW = pdf.internal.pageSize.getWidth();
  pdf.addImage(
    canvas.toDataURL("image/png"),
    "PNG",
    0,
    0,
    pdfW,
    (canvas.height * pdfW) / canvas.width,
  );

  pdf.save(`relatorio-${restaurantName}-${startDate}-${endDate}.pdf`);
};
