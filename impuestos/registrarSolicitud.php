<?php
header('Content-Type: application/json');

require_once "configuracion.php";


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

$requiredFields = ['rucEmisor', 'claveAccesoComprobante', 'idBeneficiario', 'codigoBeneficio', 'baseImponible', 'porcentajeIva', 'montoIva', 'estado', 'mensajeSolicitud' ];
foreach ($requiredFields as $field) {
    if (!isset($datosBeneficio[$field])) {
        echo json_encode([
            'codigo' => 'ERROR',
            'mensaje' => "El campo $field es requerido"
        ]);
        exit;
    }
}

try {

    try {
        $pdo = new PDO("mysql:host=50.31.188.7;dbname=$base_de_datos", $usuario, $contrasena);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        error_log('Error de conexión: ' . $e->getMessage());
        echo json_encode([
            'codigo' => 'ERROR',
            'mensaje' => 'Error de conexión a la base de datos'
        ]);
        exit;
    }

    $stmt = $pdo->prepare("INSERT INTO solicitudesDevolucionImpuesto (rucEmisor, claveAcceso, idBeneficiario, codigoBeneficio, baseImponible, porcentajeIva, montoIva,  estado, mensajeSolicitud) VALUES (:rucEmisor, :claveAccesoComprobante, :idBeneficiario, :codigoBeneficio, :baseImponible, :porcentajeIva, :montoIva,  :estado, :mensajeSolicitud)");

    // Vincular los valores de los parámetros
    $stmt->bindParam(':rucEmisor', $datosBeneficio['rucEmisor']);
    $stmt->bindParam(':claveAccesoComprobante', $datosBeneficio['claveAccesoComprobante']);
    $stmt->bindParam(':idBeneficiario', $datosBeneficio['idBeneficiario']);
    $stmt->bindParam(':codigoBeneficio', $datosBeneficio['codigoBeneficio']);
    $stmt->bindParam(':baseImponible', $datosBeneficio['baseImponible']);
    $stmt->bindParam(':porcentajeIva', $datosBeneficio['porcentajeIva']);
    $stmt->bindParam(':montoIva', $datosBeneficio['montoIva']);
    $stmt->bindParam(':estado', $datosBeneficio['estado']);
    $stmt->bindParam(':mensajeSolicitud', $datosBeneficio['mensajeSolicitud']);

    // Ejecutar la declaración SQL
    $stmt->execute();

    // Calcular el monto de IVA a devolver (en este caso, suponemos que es el mismo monto recibido)
    $montoIvaDevolver = number_format((float)$datosBeneficio['montoIva'], 2, '.', '');

    // Construir la respuesta
    $response = [
        'codigo' => 'OK',
        'mensaje' => 'Se ha procesado correctamente la solicitud de registro.'
    ];

    // Devolver la respuesta como JSON
    echo json_encode($response);
} catch (PDOException $e) {
    echo json_encode([
        'codigo' => 'ERROR',
        'mensaje' => 'Error al insertar los datos en la base de datos: ' . $e->getMessage()
    ]);
}
?>
