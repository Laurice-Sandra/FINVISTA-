<!-- src/main/resources/templates/email-template.ftl -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insurance Confirmation</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; }
        .container { width: 80%; margin: auto; padding: 20px; }
        .header { background: #800020; padding: 20px; text-align: center; color: #ffffff; }
        .content { margin-top: 20px; }
        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #28a745;
            color: #ffffff;
            text-decoration: none;
            border-radius: 5px;
        }
        .footer { margin-top: 20px; padding: 20px; background: #f4f4f4; text-align: center; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Insurance Confirmation</h2>
    </div>
    <div class="content">
        <p>Hello ,</p>
        <p>We are pleased to inform you that your Insurance Contrat has been approved. Attached you will find the logo confirme. Please read it carefully.</p>
        <p>To finalize the process and proceed with the fund transfer, please confirm the acceptance of the contract by clicking the link below:</p>
        <p><a href="" class="button" target="_blank">Confirm My Insurance</a></p>
        <p>If you have any questions or need assistance, do not hesitate to contact us.</p>
        <p>Thank you for your trust,</p>
        <p>The FinvistaFlexiFin Team</p>
    </div>
    <div class="footer">
        <p>This message was sent automatically, please do not reply directly to it.</p>
    </div>
</div>
</body>
</html>