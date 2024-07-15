<?php
header('Content-Type: application/json');

// Leer el contenido de la solicitud
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Verificar si los datos necesarios están presentes
if (!isset($data['DatosBeneficio'])) {
    echo json_encode([
        'codigo' => 'ERROR',
        'mensaje' => 'DatosBeneficio no proporcionado'
    ]);
    exit;
}

$datosBeneficio = $data['DatosBeneficio'];

$requiredFields = ['rucEmisor', 'claveAccesoComprobante', 'idBeneficiario', 'codigoBeneficio', 'baseImponible', 'porcentajeIva', 'montoIva'];
foreach ($requiredFields as $field) {
    if (!isset($datosBeneficio[$field])) {
        echo json_encode([
            'codigo' => 'ERROR',
            'mensaje' => "El campo $field es requerido"
        ]);
        exit;
    }
}

// Simular un tiempo de espera de 5 segundos
sleep(5);

// Calcular el monto de IVA a devolver (en este caso, suponemos que es el mismo monto recibido)
$montoIvaDevolver = number_format((float)$datosBeneficio['montoIva'], 2, '.', '');

// Construir la respuesta
$response = [
    'montoIvaDevolver' => $montoIvaDevolver,
    'codigo' => 'EXITO',
    'mensaje' => 'Operación realizada correctamente'
];

// Devolver la respuesta como JSON
echo json_encode($response);
?>
